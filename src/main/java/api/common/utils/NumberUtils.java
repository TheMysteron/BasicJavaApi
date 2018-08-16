package api.common.utils;

import api.common.exceptions.InternalServerErrorException;

import java.math.BigDecimal;

/**
 * NumberUtils - wrapper class providing utility methods to get or manipulate numbers
 */
public class NumberUtils {

    /**
     * Helper method to do a null check prior to conversion to a BigDecimal
     *
     * @param input - byte array to convert to a BigDecimal
     * @return - the BigDecimal, or BigDecimal value of 0
     */
    public static BigDecimal toDecimal(String input) {

        BigDecimal newValue = BigDecimal.ZERO;
        if (input != null && input.length() > 0) {
            try {
                newValue = new BigDecimal(input);
            } catch (NumberFormatException e){
                throw new InternalServerErrorException(
                        String.format("Value %s is not a valid number", input), e);
            }
        }
        return newValue;
    }

    /**
     * Helper method to do a null check prior to conversion to a BigDecimal
     *
     * @param input - byte array to convert to a BigDecimal
     * @return - the BigDecimal, or Null
     */
    public static BigDecimal toDecimalNull(String input) {
        BigDecimal newValue = null;
        if (input != null && input.length() > 0) {
            try {
                newValue = new BigDecimal(input);
            } catch (NumberFormatException e){
                throw new InternalServerErrorException(
                        String.format("Value %s is not a valid number", input), e);
            }
        }
        return newValue;
    }
}
