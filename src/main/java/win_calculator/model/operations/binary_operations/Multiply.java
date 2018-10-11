package win_calculator.model.operations.binary_operations;

import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

/**
 * Entity class for multiply operation at {@link win_calculator.model.CalcModel}
 */
public class Multiply implements BinaryOperation {

    /**
     * Overridden method from {@link BinaryOperation}
     * Calculate multiply operation on given BigDecimal number with itself
     * @param number - given BigDecimal number
     * @return BigDecimal result of calculation
     */
    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.multiply(number);
    }

    /**
     * Overridden method from {@link BinaryOperation}
     * Calculate multiply operation on given BigDecimal firstNumber with given BigDecimal secondNumber
     * @param firstNumber - given first BigDecimal number
     * @param secondNumber - given second BigDecimal number
     * @return BigDecimal result of calculation
     */
    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber);
    }

    @Override
    public OperationType getType() {
        return OperationType.MULTIPLY;
    }


}
