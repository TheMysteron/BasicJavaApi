package api.presentation.controller;

import api.business.Service;
import api.business.model.Response;
import api.common.exceptions.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ControllerTest {

    @InjectMocks
    private Controller controller;

    @Mock
    private Service service;

    private final String CORRELATION_ID = "CorrelationId";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = BadRequestException.class)
    public void getIdIsInvalid() {

        try {
            controller.getInfo(CORRELATION_ID, "XXXXX");
        } catch (IllegalArgumentException iae) {
            verifyZeroInteractions(service);
            throw iae;
        }
    }

    @Test
    public void getIdIsValid() {

        try {
            controller.getInfo(CORRELATION_ID, "AJ99999");
        } catch (IllegalArgumentException iae) {
            verifyZeroInteractions(service);
            throw iae;
        }

    }

    @Test
    public void checkCorrelationPopulated() {

        ResponseEntity<Response> response = null;
        when(controller.getInfo("12345678901234567890", "AB123456")).thenReturn(response);
        response = controller.getInfo("12345678901234567890", "AB123456");
        assertEquals("12345678901234567890", response.getHeaders().get("CorrelationId").iterator().next());

    }
}