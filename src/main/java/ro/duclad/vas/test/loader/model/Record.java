package ro.duclad.vas.test.loader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import static ro.duclad.vas.test.loader.model.RecordConstants.CALL_STATUS_KO;
import static ro.duclad.vas.test.loader.model.RecordConstants.CALL_STATUS_OK;
import static ro.duclad.vas.test.loader.model.RecordConstants.CALL_TYPE;
import static ro.duclad.vas.test.loader.model.RecordConstants.MSG_STATUS_DELIVERED;
import static ro.duclad.vas.test.loader.model.RecordConstants.MSG_STATUS_SEEN;
import static ro.duclad.vas.test.loader.model.RecordConstants.MSG_TYPE;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Record {
    @JsonProperty("message_type")
    private String messageType;
    private Long timestamp;
    private Long origin;
    private Long destination;
    private Long duration;
    @JsonProperty("status_code")
    private String statusCall;
    @JsonProperty("status_description")
    private String statusDescription;
    @JsonProperty("message_content")
    private String messageContent;
    @JsonProperty("message_status")
    private String messageStatus;


    /**
     * Is assumed that the following fields must be present: messageType, timestamp, origin, destination
     *
     * @return true if any of the previous fields are missing.
     */
    public boolean hasMissingFields() {
        return messageType == null || timestamp == null || origin == null || destination == null;
    }

    /**
     * @return true if the records is a message and there is no content or if is a call and duration is 0, false otherwise
     */
    public boolean hasEmptyMessageContent() {
        return (MSG_TYPE.equalsIgnoreCase(messageType) && StringUtils.isEmpty(messageContent)) || (CALL_TYPE.equalsIgnoreCase(messageType) && duration == null);
    }

    /**
     * Is assumed that any of this cases will constitute a field error:
     * - origin number not a valid MSISDN (we check for now only the presence of the country code)
     * - destination number not a valid MSISDN (we check for now only the presence of the country code)
     * - message type must be CALL or MSG
     * - if CALL type the status call must be present and must have OK or KO values
     * - if MSG type the message status must be present and must be DELIVERED or SEEN
     *
     * @return true is any of the previous conditions are met
     */
    public boolean hasFieldsErrors() {
        if (getOriginCountryCode() == null || getDestinationCountryCode() == null) {
            return true;
        }
        if (!CALL_TYPE.equalsIgnoreCase(messageType) && !MSG_TYPE.equalsIgnoreCase(messageType)) {
            return true;
        }
        if (CALL_TYPE.equalsIgnoreCase(messageType)) {
            if (StringUtils.isEmpty(statusCall)) {
                return true;
            }
            if (!CALL_STATUS_OK.equalsIgnoreCase(statusCall) && CALL_STATUS_KO.equalsIgnoreCase(statusCall)) {
                return true;
            }
        }
        if (MSG_TYPE.equalsIgnoreCase(messageType)) {
            if (StringUtils.isEmpty(messageStatus)) {
                return true;
            }
            if (!MSG_STATUS_DELIVERED.equalsIgnoreCase(messageStatus) && !MSG_STATUS_SEEN.equalsIgnoreCase(messageStatus)) {
                return true;
            }
        }
        return false;
    }

    public Integer getOriginCountryCode() {
        try {
            return PhoneNumberUtil.getInstance().parse("+" + origin, null).getCountryCode();
        } catch (NumberParseException e) {
            return null;
        }
    }

    public Integer getDestinationCountryCode() {
        try {
            return PhoneNumberUtil.getInstance().parse("+" + destination, null).getCountryCode();
        } catch (NumberParseException e) {
            return null;
        }
    }

}
