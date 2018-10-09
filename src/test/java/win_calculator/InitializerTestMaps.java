package win_calculator;

import win_calculator.controller.entities.Digit;
import win_calculator.model.operations.enter.Equal;
import win_calculator.model.operations.extra_operations.*;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.clear.BaskSpace;
import win_calculator.model.operations.clear.Clear;
import win_calculator.model.operations.clear.ClearEntered;
import win_calculator.model.operations.main_operations.Add;
import win_calculator.model.operations.main_operations.Divide;
import win_calculator.model.operations.main_operations.Multiply;
import win_calculator.model.operations.main_operations.Subtract;
import win_calculator.model.operations.memory_operations.*;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import static win_calculator.controller.enums.DigitType.*;

/**
 * Abstract class initializes test maps
 */
abstract class InitializerTestMaps {

    /**
     * Method initializes map of buttons for tests by keyboard
     * @return HashMap where String is button id, ButtonForTest is entity of test button
     */
    static HashMap<String, ButtonForTest> createButtonsMap() {

        HashMap<String, ButtonForTest> map = new HashMap<>();
        map.put("0", new ButtonForTest("#zeroBtn",KeyEvent.VK_0, false));
        map.put("1", new ButtonForTest("#oneBtn",KeyEvent.VK_1, false));
        map.put("2", new ButtonForTest("#twoBtn",KeyEvent.VK_2, false));
        map.put("3", new ButtonForTest("#threeBtn",KeyEvent.VK_3, false));
        map.put("4", new ButtonForTest("#fourBtn",KeyEvent.VK_4, false));
        map.put("5", new ButtonForTest("#fiveBtn",KeyEvent.VK_5, false));
        map.put("6", new ButtonForTest("#sixBtn",KeyEvent.VK_6, false));
        map.put("7", new ButtonForTest("#sevenBtn",KeyEvent.VK_7, false));
        map.put("8", new ButtonForTest("#eightBtn",KeyEvent.VK_8, false));
        map.put("9", new ButtonForTest("#nineBtn",KeyEvent.VK_9, false));
        map.put(",", new ButtonForTest("#comaBtn",KeyEvent.VK_COMMA, false));
        map.put("+", new ButtonForTest("#addBtn",KeyEvent.VK_ADD, false));
        map.put("-", new ButtonForTest("#subtractBtn",KeyEvent.VK_SUBTRACT, false));
        map.put("*", new ButtonForTest("#multiplyBtn",KeyEvent.VK_8, true));
        map.put("n*", new ButtonForTest("#multiplyBtn",KeyEvent.VK_MULTIPLY, false));
        map.put("/", new ButtonForTest("#divideBtn",KeyEvent.VK_DIVIDE, false));
        map.put("%", new ButtonForTest("#percentBtn",KeyEvent.VK_5, true));
        map.put("sqrt", new ButtonForTest("#sqrtBtn",KeyEvent.VK_2, true));
        map.put("sqr", new ButtonForTest("#sqrBtn",KeyEvent.VK_Q, false));
        map.put("1/x", new ButtonForTest("#fractionBtn",KeyEvent.VK_R, false));
        map.put("CE", new ButtonForTest("#clearEnteredBtn",KeyEvent.VK_DELETE, false));
        map.put("C", new ButtonForTest("#clearBtn",KeyEvent.VK_C, false));
        map.put("⟵", new ButtonForTest("#backSpaceBtn",KeyEvent.VK_BACK_SPACE, false));
        map.put("=", new ButtonForTest("#equalsBtn",KeyEvent.VK_EQUALS, false));
        map.put("±", new ButtonForTest("#negateBtn",KeyEvent.VK_F9, false));
        map.put("MC", new ButtonForTest("#clearAllMemoryBtn",KeyEvent.VK_L, false));
        map.put("MS", new ButtonForTest("#memoryStoreBtn",KeyEvent.VK_M, false));
        map.put("MR", new ButtonForTest("#memoryRecallBtn",KeyEvent.VK_O, false));
        map.put("M+", new ButtonForTest("#memoryAddBtn",KeyEvent.VK_P, false));
        map.put("M-", new ButtonForTest("#memorySubtractBtn",KeyEvent.VK_S, false));
        map.put("MENU", new ButtonForTest("#menuBtn",KeyEvent.VK_M, true));
        map.put("FS", new ButtonForTest("#fullScreenBtn",KeyEvent.VK_F, true));
        map.put("HD", new ButtonForTest("#hideBtn",KeyEvent.VK_H, true));
        return map;
    }

    /**
     * Method initializes map of operations
     * @return HashMap where String is operation's id using in test expressions
     */
    static HashMap<String, Operation> createOperationsMap() {

        HashMap<String, Operation> map = new HashMap<>();
        map.put("+", new Add());
        map.put("-", new Subtract());
        map.put("*", new Multiply());
        map.put("/", new Divide());
        map.put("%", new Percent());
        map.put("sqrt", new Sqrt());
        map.put("sqr", new Sqr());
        map.put("1/x", new Fraction());
        map.put("CE", new ClearEntered());
        map.put("C", new Clear());
        map.put("⟵", new BaskSpace());
        map.put("=", new Equal());
        map.put("±", new Negate());
        map.put("MC", new ClearMemory());
        map.put("MS", new StoreMemory());
        map.put("MR", new RecallMemory());
        map.put("M+", new AddToMemory());
        map.put("M-", new SubtractMemory());
        return map;
    }

    /**
     * Method initializes map of digits
     * @return HashMap where String is digit's id using in test expressions
     */
    static HashMap<String, Digit> createDigitsMap() {

        HashMap<String, Digit> map = new HashMap<>();
        map.put("0", new Digit(ZERO));
        map.put("1", new Digit(ONE));
        map.put("2", new Digit(TWO));
        map.put("3", new Digit(THREE));
        map.put("4", new Digit(FOUR));
        map.put("5", new Digit(FIVE));
        map.put("6", new Digit(SIX));
        map.put("7", new Digit(SEVEN));
        map.put("8", new Digit(EIGHT));
        map.put("9", new Digit(NINE));
        map.put(",", new Digit(COMA));
        return map;
    }

}
