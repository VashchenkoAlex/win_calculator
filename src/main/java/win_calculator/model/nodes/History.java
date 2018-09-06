package win_calculator.model.nodes;

import win_calculator.model.nodes.actions.Action;
import win_calculator.controller.nodes.digits.Number;

import java.util.LinkedList;

import static win_calculator.utils.ActionType.MAIN_OPERATION;
import static win_calculator.utils.ActionType.NUMBER;

public class History {

    private LinkedList<Action> actions;

    public History() {

        actions = new LinkedList<>();
    }

    public void addAction(Action action){

        actions.add(action);
    }

    public void setActions(LinkedList<Action> actions){

        this.actions = actions;
    }

    public LinkedList<Action> getActions() {

        return actions;
    }

    public Action getLastAction(){

        if (actions.isEmpty()){
            return null;
        }else {
            return actions.getLast();
        }
    }

    public void changeLastAction(Action action){

        if (!actions.isEmpty()){
            actions.set(actions.size()-1,action);
        }else {
            actions.add(action);
        }
    }

    public void changeLastNumber(Number number){

        if (isChangingPossible()){
            for (int i = actions.size()-1; i >0 ; i--) {
                if (NUMBER.equals(actions.get(i).getType())){
                    actions.set(i,number);
                    break;
                }
            }
        }else {
            actions.add(number);
        }

    }

    private boolean isChangingPossible(){

        boolean result = false;
        for (Action action :actions) {
            if (NUMBER.equals(action.getType())){
                result = true;
                break;
            }
        }
        result = result && !MAIN_OPERATION.equals(getLastAction().getType());
        return result;
    }
}
