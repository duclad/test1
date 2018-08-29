package ro.duclad.vas.test.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ro.duclad.vas.test.dao.model.DayMetrics;
import ro.duclad.vas.test.dao.model.ServiceKips;
import ro.duclad.vas.test.loader.DataLoadingException;
import ro.duclad.vas.test.service.ApplicationService;

import java.util.Date;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping("/metrics/{date}")
    public DayMetrics metrics(@PathVariable(value = "date") String date) {
        return applicationService.getDayMetrics(date).orElseThrow(() -> new RuntimeException(""));
    }

    @RequestMapping("/kips")
    public ServiceKips kips() {
        return applicationService.getServiceKips();
    }

    @ExceptionHandler({DataLoadingException.class})
    public ResponseEntity<ErrorDetails> handlingDataLoadingException(DataLoadingException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
