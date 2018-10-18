package win_calculator.model;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.memory_operations.MemoryOperation;
import win_calculator.model.operations.memory_operations.MemoryOperationType;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.Number;
import win_calculator.model.operations.extra_operations.ExtraOperation;
import win_calculator.model.operations.extra_operations.Negate;
import win_calculator.model.operations.OperationType;
import win_calculator.model.operations.percent.Percent;
import win_calculator.model.operations.binary_operations.BinaryOperation;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.model.operations.memory_operations.MemoryOperationType.*;
import static win_calculator.model.operations.memory_operations.MemoryOperationType.SUBTRACT_FROM_MEMORY;
import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.utils.ModelUtils.isBinaryOperation;
import static win_calculator.model.utils.ModelUtils.isExtraOperation;

/**
 * Class processes calculator operations
 * Initializes the {@link History} instance
 * Initializes the {@link Memory} instance
 * Provides methods for calculator operations
 * Return BigDecimal lastBinaryResult of calculations
 */
class OperationProcessor {

   /**
    * The instance of {@link History}
    */
   private History history = new History();
   /**
    * The instance of {@link Memory}
    */
   private Memory memory = new Memory();

   /**
    * Stores {@link OperationType} of last operation
    */
   private OperationType lastOperationType = OperationType.CLEAR;
   /**
    * Stores last binary operation for calculations
    */
   private BinaryOperation lastBinaryOperation;

   /**
    * Stores last used number at calculations
    */
   private BigDecimal lastNumber;
   /**
    * Stores previous used number at calculations
    */
   private BigDecimal previousNumber;
   /**
    * Stores result number of last binary operation
    */
   private BigDecimal lastBinaryResult;
   /**
    * Stores result number of last extra operation
    */
   private BigDecimal lastExtraResult;
   /**
    * Stores result number of last operation
    */
   private BigDecimal operationResult;
   /**
    * Stores last inputted number
    */
   private BigDecimal lastInputtedNumber;

   /**
    * Stores was it equal repeated last time for binary operations
    */
   private boolean enterForOperationRepeated;
   /**
    * Stores was it equal repeated last time
    */
   private boolean enterRepeated;
   /**
    * Stores was it binary operation before
    */
   private boolean mOperationBefore = false;

   /**
    * Rejects last added number and extra operations on it from the history
    *
    * @return true if rejection was successful
    */
   boolean rejectLastNumberWithExtraOperations() {

      boolean done;
      if (historyNotEmpty() && isHistoryContainingExtraPercentNegate()) {

         LinkedList<Operation> operations = new LinkedList<>(history.getOperations());
         OperationType type;
         for (int i = operations.size() - 1; i > 0; i--) {
            type = operations.getLast().getType();

            if (isExtraOperation(type) || NEGATE == type || PERCENT == type) {
               operations.removeLast();
            }

            if (NUMBER == type) {
               break;
            }

         }

         operations.removeLast();
         history.setOperations(operations);
         lastNumber = null;
         setLastExtraResult(null);
         done = true;
      } else {
         done = false;
      }

      return done;
   }

   /**
    * Selects number for the {@link Negate} operation in depends on previous operation
    * Sends selected number to the history
    * Calls method calculate() at {@link Negate} with selected number
    *
    * @param negate         - given {@link Negate} operation
    * @param inputtedNumber - given last inputted number
    * @param responseNumber - given last response number
    * @return BigDecimal result of calculations
    */
   BigDecimal processNegate(Operation negate, BigDecimal inputtedNumber, BigDecimal responseNumber) {

      BigDecimal result;
      if (inputtedNumber == null && lastOperationNotNumberAndNotNegate()) {

         if (responseNumber != null) {
            result = responseNumber;

            if (isNumberHasToBeAdded()) {
               addNumberToHistory(result);
               setLastNumber(result);
            }

         } else {
            result = BigDecimal.ZERO;
            addNumberToHistory(result);
            setLastNumber(result);
         }

         resetLastBinaryResult();

      } else {
         result = lastNumber;
      }

      result = ((Negate) negate).calculate(result);
      addOperationToHistory(negate);
      lastNumber = result;

      return result;
   }

   /**
    * Verifies is last operation type allows to add number to history
    *
    * @return boolean verifications result
    */
   private boolean isNumberHasToBeAdded() {

      return isBinaryOperation(lastOperationType) || CLEAR == lastOperationType;
   }

   /**
    * Getter for operations from the history
    *
    * @return LinkedList<Operation> of operations from the history
    */
   LinkedList<Operation> getHistory() {

      return history.getOperations();
   }

   /**
    * Selects number for the {@link ExtraOperation} in depends on inputted number
    * and previous result
    * Sends selected number, current operation and result of calculation to the history
    * Calls method calculate() at {@link ExtraOperation} with selected number
    *
    * @param operation      - given {@link ExtraOperation} operation
    * @param inputtedNumber - given last inputted number
    * @param responseNumber - given last response number
    * @return BigDecimal result of calculations
    */
   BigDecimal processExtraOperation(Operation operation, BigDecimal inputtedNumber, BigDecimal responseNumber) throws OperationException {

      BigDecimal result = prepareNumberForExtraOperation(inputtedNumber, responseNumber);
      addOperationToHistory(operation);
      result = ((ExtraOperation) operation).calculate(result);
      setLastExtraResult(result);
      return result;
   }

   /**
    * Selects number for extra operation by given inputted number, last response number,
    * and last operation
    * Stores it to the history
    * @param inputtedNumber - given BigDecimal inputted number
    * @param responseNumber - given BigDecimal last response number
    * @return selected number
    */
   private BigDecimal prepareNumberForExtraOperation(BigDecimal inputtedNumber, BigDecimal responseNumber) {

      BigDecimal resultNum;
      if (inputtedNumber != null) {
         lastInputtedNumber = inputtedNumber;
         addNumberToHistory(inputtedNumber);
         setLastNumber(inputtedNumber);
         resultNum = inputtedNumber;
      } else if (responseNumber != null) {
         resultNum = responseNumber;

         if (isResponseNumberHasProceed()) {
            addNumberToHistory(resultNum);
            setLastNumber(resultNum);
            resetLastBinaryResult();
         }

      } else {
         resultNum = BigDecimal.ZERO;
         addZeroToHistory();
      }

      return resultNum;
   }

   /**
    * Verifies is response number has to be proceed by last operation
    * @return boolean verification result
    */
   private boolean isResponseNumberHasProceed() {

      return isBinaryOperation(lastOperationType) ||
              (enterRepeated && !isExtraOperation(lastOperationType) && NEGATE != lastOperationType);
   }

   /**
    * Selects number for the {@link BinaryOperation} in depends on inputted number
    * and previous result
    * Sends selected number and current operation to the history
    * Calls method doCalculation() with selected number
    *
    * @param operation      - given {@link BinaryOperation} operation
    * @param inputtedNumber - given last inputted number
    * @param responseNumber - given last response number
    * @return BigDecimal result of calculations
    */
   BigDecimal processBinaryOperation(Operation operation, BigDecimal inputtedNumber, BigDecimal responseNumber) throws OperationException {

      BigDecimal result = responseNumber;
      if (inputtedNumber != null) {
         addNumberToHistory(inputtedNumber);
         setLastNumber(inputtedNumber);
         result = inputtedNumber;
         lastInputtedNumber = inputtedNumber;

         if (enterRepeated) {
            resetLastBinaryResult();
         }

         if (!mOperationBefore) {
            resetPreviousNumber();
         }

      } else if (responseNumber != null) {
         if (!history.isContainingExtraOperation()) {
            changeLastNumberAtEvents(result);
         }

      } else {
         addZeroToHistory();
         result = BigDecimal.ZERO;
      }

      doBinaryOperation(operation);
      if (operationResult != null) {
         result = operationResult;
      }

      enterRepeated = false;
      resetLastExtraResult();
      return result;
   }

   /**
    * Cleans lastNumber, previousNumber, operationResult,
    * enterForOperationRepeated, mOperationBefore, history,
    * lastBinaryResult, enterRepeated, lastExtraResult and lastInputtedNumber
    *
    * @return null
    */
   BigDecimal processClear() {

      lastNumber = null;
      resetPreviousNumber();
      resetOperationResult();
      enterForOperationRepeated = false;
      mOperationBefore = false;
      resetHistory();
      resetLastBinaryResult();
      enterRepeated = false;
      lastExtraResult = null;
      lastInputtedNumber = null;
      return null;
   }

   /**
    * Selects number for the last {@link BinaryOperation} in depends on inputted number
    * and previous result
    * Sets up global variables before calculation
    * Calls method doEnter()
    * Cleans history, lastExtraResult and lastOperationType
    *
    * @param inputtedNumber - given last inputted number
    * @param responseNumber - given last response number
    * @return BigDecimal result of calculations
    */
   BigDecimal processEnter(BigDecimal inputtedNumber, BigDecimal responseNumber) throws OperationException {

      BigDecimal result = responseNumber;
      if (inputtedNumber != null) {
         resetLastBinaryResult();
         setLastNumber(inputtedNumber);
         result = inputtedNumber;
         lastInputtedNumber = inputtedNumber;
      } else {
         if (PERCENT.equals(lastOperationType)) {
            lastBinaryResult = previousNumber;
         }
         if (isExtraOperation(lastOperationType) && enterRepeated) {
            lastBinaryResult = lastInputtedNumber;
         }
         if (!enterRepeated && isBinaryResultNotNull() && (isBinaryOperation(lastOperationType) || MEMORY.equals(lastOperationType))) {
            setLastNumber(lastBinaryResult);
         }
      }
      initVariables();
      if (mOperationBefore || enterForOperationRepeated) {
         doCalculation();
         enterForOperationRepeated = true;
         mOperationBefore = false;
      } else {
         resetOperationResult();
      }
      if (operationResult != null) {
         result = operationResult;
      }
      resetPreviousNumber();
      enterRepeated = true;
      resetLastExtraResult();
      lastOperationType = OperationType.CLEAR;
      resetHistory();
      return result;
   }

   /**
    * Verifies type of memory operation
    * Cleans memory if it's necessary
    * Selects number for memory
    * Calls method doMemoryOperation() with current memory operation and selected number
    *
    * @param operation      - given {@link MemoryOperation}
    * @param inputtedNumber - given last inputted number
    * @return BigDecimal result of memory operation
    */
   BigDecimal processMemory(MemoryOperation operation, BigDecimal inputtedNumber) {

      MemoryOperationType memoryOperationType = operation.getMemoryOperationType();
      BigDecimal responseNumber = inputtedNumber;
      if (CLEAR_MEMORY.equals(memoryOperationType)) {
         memory = new Memory();
      } else {
         if (responseNumber != null) {
            lastInputtedNumber = inputtedNumber;
         }
         if (responseNumber == null && isExtraResultNotNull()) {
            responseNumber = lastExtraResult;
         }
         if (responseNumber == null && operationResult != null) {
            responseNumber = operationResult;
         }
         if (responseNumber == null && lastNumber != null) {
            responseNumber = lastNumber;
         }
         if (responseNumber == null) {
            responseNumber = BigDecimal.ZERO;
         }
         BigDecimal result = doMemoryOperation(operation, responseNumber);
         if (result != null) {
            responseNumber = result;
         }
      }
      lastOperationType = operation.getType();
      return responseNumber;
   }

   /**
    * Sets up last inputted number if it's necessary
    * Calls doPercent() method with given operation and given number
    * Handles result of percent calculation and sets up global fields depends on it
    * Sends result and operation to the history
    *
    * @param percent - given operation for calculation
    * @param number  - given inputted number
    * @return BigDecimal result of calculation
    */
   BigDecimal processPercent(Operation percent, BigDecimal number) {

      if (number != null) {
         lastInputtedNumber = number;
      }
      BigDecimal response = doPercent((Percent) percent, number);
      if (!BigDecimal.ZERO.equals(response) && !"0.00".equals(response.toString())) {
         if (previousNumber == null) {
            previousNumber = lastNumber;
         } else if (number != null) {
            previousNumber = lastBinaryResult;
         }
         rejectLastNumberWithExtraOperations();
         addNumberToHistory(response);
         lastNumber = response;
         enterRepeated = false;
         addOperationToHistory(percent);
      } else if (isBinaryOperation(lastOperationType) || isExtraOperation(lastOperationType)) {
         addNumberToHistory(BigDecimal.ZERO);
         setLastNumber(number);
         resetLastBinaryResult();
      } else {
         addZeroToHistory();
      }
      return response;
   }

   /**
    * Cleans last inputted number from the history and global fields
    */
   void processClearEntered() {

      lastInputtedNumber = null;
      boolean done = rejectLastNumberWithExtraOperations();
      if (!done) {
         rejectLastNumber();
      }
      if (!enterRepeated) {
         lastExtraResult = operationResult;
         if (lastExtraResult == null) {
            lastExtraResult = previousNumber;
         }
      }
   }

   /**
    * Resets previousNumber
    */
   private void resetPreviousNumber() {
      previousNumber = null;
   }

   /**
    * Resets operationResult
    */
   private void resetOperationResult() {
      operationResult = null;
   }

   /**
    * Resets history
    */
   private void resetHistory() {
      history = new History();
   }

   /**
    * Rejects last number at operations from the history
    */
   private void rejectLastNumber() {

      if (historyNotEmpty() && !lastOperationNotNumberAndNotNegate()) {
         LinkedList<Operation> operations = new LinkedList<>(history.getOperations());
         OperationType type;
         for (int i = operations.size() - 1; i > 0; i--) {
            type = operations.get(i).getType();
            if (NUMBER == type) {
               operations.removeLast();
               break;
            }
         }
         history.setOperations(operations);
      }
   }

   /**
    * Last extra result setter
    *
    * @param lastExtraResult - BigDecimal value for last extra result
    */
   private void setLastExtraResult(BigDecimal lastExtraResult) {
      this.lastExtraResult = lastExtraResult;
   }

   /**
    * Resets last extra result and saves it value to the last number
    */
   private void resetLastExtraResult() {
      if (isExtraResultNotNull()) {
         lastNumber = lastExtraResult;
      }
      lastExtraResult = null;
   }

   /**
    * Verifies is last operation not number or not negate
    *
    * @return true if last operation not number or not negate
    */
   private boolean lastOperationNotNumberAndNotNegate() {

      boolean result;
      if (lastOperationType != null) {
         result = NUMBER != lastOperationType && NEGATE != lastOperationType;
      } else {
         result = true;
      }
      return result;
   }

   /**
    * Verifies is operation's list at history not empty
    *
    * @return true if operation's list at history is not empty
    */
   private boolean historyNotEmpty() {

      return !history.getOperations().isEmpty();
   }

   /**
    * Adds zero number to the operations at history
    */
   private void addZeroToHistory() {

      Number number = new Number(BigDecimal.ZERO);
      if (!historyNotEmpty()) {
         history.addOperation(number);
      } else {
         history.changeNumberAtFirstPosition(number);
      }
      lastNumber = BigDecimal.ZERO;
      mOperationBefore = false;
   }

   /**
    * Resets last binary result
    */
   private void resetLastBinaryResult() {

      lastBinaryResult = null;
   }

   /**
    * Verifies is operations at history contains {@link Negate} or {@link Percent} or {@link Negate}
    *
    * @return boolean result of verifying
    */
   private boolean isHistoryContainingExtraPercentNegate() {

      boolean isContaining = history.isContainingGivenOperationType(NEGATE);
      isContaining = isContaining || history.isContainingExtraOperation();
      isContaining = isContaining || history.isContainingGivenOperationType(PERCENT);

      return isContaining;
   }

   /**
    * Selects two numbers for {@link Percent} operation by the given number and global fields
    *
    * @param number - given inputted number
    * @return BigDecimal[] of selected numbers
    */
   private BigDecimal[] selectNumbersForPercent(BigDecimal number) {

      BigDecimal firstNumber;
      BigDecimal secondNumber;
      if (number != null) {
         secondNumber = number;

         if (isBinaryResultNotNull()) {
            firstNumber = lastBinaryResult;

         } else if (mOperationBefore && NUMBER != lastOperationType) {
            firstNumber = lastNumber;

         } else {
            firstNumber = previousNumber;

         }
      } else if (isBinaryResultNotNull()) {

         if (PERCENT == lastOperationType) {
            secondNumber = lastNumber;

         } else if (isExtraOperation(lastOperationType) && isExtraResultNotNull()) {
            secondNumber = lastExtraResult;

         } else {
            secondNumber = lastBinaryResult;

         }
         firstNumber = lastBinaryResult;

      } else if (NEGATE == lastOperationType) {
         firstNumber = previousNumber;
         secondNumber = lastNumber;

      } else if (isLastNumberNotNull()) {
         firstNumber = lastNumber;

         if (previousNumber != null && PERCENT == lastOperationType && mOperationBefore) {
            secondNumber = previousNumber;

         } else if (isExtraResultNotNull()) {
            secondNumber = lastExtraResult;

         } else {
            secondNumber = lastNumber;

         }
      } else {
         firstNumber = BigDecimal.ZERO;
         secondNumber = BigDecimal.ZERO;

      }

      return new BigDecimal[]{firstNumber, secondNumber};
   }

   /**
    * Verifies is lastNumber not null
    *
    * @return true if last number not null
    */
   private boolean isLastNumberNotNull() {
      return lastNumber != null;
   }

   /**
    * Calls selectNumbersForPercent() method with given inputted number
    * and sends result to the method calculate() at {@link Percent}
    * Handles result of calculation
    *
    * @param percent - given operation
    * @param number  - given inputted number
    * @return BigDecimal result of calculation
    */
   private BigDecimal doPercent(Percent percent, BigDecimal number) {

      BigDecimal[] numbers = selectNumbersForPercent(number);
      BigDecimal result;
      if (numbers[0] != null) {
         result = percent.calculate(numbers[0], numbers[1]);
      } else {
         result = BigDecimal.ZERO;
      }

      return result;
   }

   /**
    * Initializes global fields before calculation
    * Calls doCalculation() method and handles result
    * Stores given binary operation to the history
    * Sets up global fields after calculation
    *
    * @param operation - given binary operation
    * @throws OperationException from doCalculation() method
    */
   private void doBinaryOperation(Operation operation) throws OperationException {

      initVariables();
      enterForOperationRepeated = false;
      if (isBinaryOperation(lastOperationType)) {
         changeLastOperation(operation);
      } else {
         addOperationToHistory(operation);
         if (mOperationBefore || enterForOperationRepeated) {
            doCalculation();
         } else {
            resetOperationResult();
         }
      }
      mOperationBefore = true;
      lastBinaryOperation = (BinaryOperation) operation;
   }

   /**
    * Selects and calls calculate() method of last {@link BinaryOperation} by global fields
    * Selects numbers for calculation
    * Stores result of calculation to the lastBinaryResult and operationResult
    *
    * @throws OperationException from calculate() method at {@link BinaryOperation}
    */
   private void doCalculation() throws OperationException {

      BigDecimal firstArg;
      if (previousNumber == null) {
         if (operationResult == null) {
            firstArg = lastNumber;

         } else {
            firstArg = operationResult;

         }
      } else {
         if (enterForOperationRepeated || operationResult != null) {
            firstArg = operationResult;

         } else {
            firstArg = previousNumber;

         }
      }
      lastBinaryResult = lastBinaryOperation.calculate(firstArg, lastNumber);
      operationResult = lastBinaryResult;
   }

   /**
    * Initializes variables for binary operation
    */
   private void initVariables() {

      if (isExtraResultNotNull()) {
         if (lastOperationNotNumberAndNotNegate()) {
            lastNumber = lastExtraResult;

         } else if (isBinaryResultNotNull() || NUMBER == lastOperationType) {
            operationResult = lastExtraResult;

         }
      }
      if (isBinaryResultNotNull()) {
         operationResult = lastBinaryResult;

      }
   }

   /**
    * Verifies is lastExtraResult not null
    *
    * @return true if lastExtraResult is not null
    */
   private boolean isExtraResultNotNull() {
      return lastExtraResult != null;
   }

   /**
    * Verifies is lastBinaryResult not null
    *
    * @return true if lastBinaryResult is not null
    */
   private boolean isBinaryResultNotNull() {
      return lastBinaryResult != null;
   }

   /**
    * Selects method from {@link Memory} by {@link MemoryOperationType} of given {@link MemoryOperation}
    *
    * @param operation - given {@link MemoryOperation}
    * @param number    - given BigDecimal number for memory operation
    * @return BigDecimal result of memory operation
    */
   private BigDecimal doMemoryOperation(MemoryOperation operation, BigDecimal number) {

      MemoryOperationType memoryOperationType = operation.getMemoryOperationType();
      BigDecimal result = null;
      if (ADD_TO_MEMORY.equals(memoryOperationType)) {
         memory.addToStoredNumber(number);

      } else if (CLEAR_MEMORY.equals(memoryOperationType)) {
         memory.clear();

      } else if (STORE.equals(memoryOperationType)) {
         memory.storeNumber(number);

      } else if (RECALL.equals(memoryOperationType)) {
         result = memory.getStoredNumber();

      } else if (SUBTRACT_FROM_MEMORY.equals(memoryOperationType)) {
         memory.subtractFromStoredNumber(number);

      }
      return result;
   }

   /**
    * Converts BigDecimal number to the {@link Number} and
    * calls addOperationToHistory() with it
    *
    * @param number - given BigDecimal number
    */
   private void addNumberToHistory(BigDecimal number) {

      addOperationToHistory(new Number(number));
   }

   /**
    * Stores given {@link Operation} to the history
    * and saves it type to the lastOperationType
    *
    * @param operation - given {@link Operation}
    */
   private void addOperationToHistory(Operation operation) {

      history.addOperation(operation);
      lastOperationType = operation.getType();
   }

   /**
    * Sends request to change last {@link BinaryOperation} at history to the given operation
    *
    * @param operation - given {@link BinaryOperation}
    */
   private void changeLastOperation(Operation operation) {

      history.addOrChangeBinaryOperation(operation);
   }

   /**
    * Saves current lastNumber to the previousNumber
    * and sets given BigDecimal number to the lastNumber
    *
    * @param number - given BigDecimal number
    */
   private void setLastNumber(BigDecimal number) {

      previousNumber = lastNumber;
      lastNumber = number;
   }

   /**
    * Converts given BigDecimal number to the {@link Number} instance
    * Changes last {@link Number} at the history to the converted
    *
    * @param number - given BigDecimal number
    */
   private void changeLastNumberAtEvents(BigDecimal number) {

      lastNumber = number;
      Number convertedNumber = new Number(number);
      if (enterRepeated && NEGATE != lastOperationType) { // TODO: 18.10.2018
         history.addOperation(convertedNumber);
      } else if (!enterRepeated && NEGATE == lastOperationType) {
         history.changeLastNumber(convertedNumber);
      }
   }

}
