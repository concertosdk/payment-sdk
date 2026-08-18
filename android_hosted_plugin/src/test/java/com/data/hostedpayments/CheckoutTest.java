package com.data.hostedpayments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

public class CheckoutTest {

    @Test
    public void normalizeAirlineObject_returnsEmptyJSONObjectWhenNull() throws Exception {
        JSONObject result = Checkout.normalizeAirlineObject(null);

        assertNotNull(result);
        assertTrue(result.length() == 0);
    }

    @Test
    public void normalizeAirlineObject_preservesProvidedAirlineData() throws Exception {
        JSONObject airline = new JSONObject();
        airline.put("bookingReference", "PNR123456");
        airline.put("transactionType", "TICKET_PURCHASE");

        JSONObject result = Checkout.normalizeAirlineObject(airline);

        assertNotNull(result);
        assertEquals("PNR123456", result.getString("bookingReference"));
        assertEquals("TICKET_PURCHASE", result.getString("transactionType"));
    }
}
