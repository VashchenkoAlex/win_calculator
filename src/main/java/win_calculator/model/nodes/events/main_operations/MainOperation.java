package win_calculator.model.nodes.events.main_operations;

import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.events.Event;

import java.math.BigDecimal;

public interface MainOperation extends Event {

    BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws MyException;

    BigDecimal calculate(BigDecimal number) throws MyException;

    String getValue();
}