package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.Divinity;

public class FSMView {
    private State state;
    private Divinity divinity;
    private boolean again;

    /**
     * constructor of the class, it set the initial state to "start"
     */
    public FSMView(){
        this.state = State.placeworker;
        this.again = false;
        this.divinity = Divinity.Default;
    }

    /**
     * Return the string to show to the player according to the fsm state
     */
    public String getStateString() {

        String line;
        switch (state) {
            case placeworker:
                line = "Place your worker on the map: (write 'placeworker [row] [column]')";
                break;
            case start:
                line = "Do you want to use the God power or going with the normal turn? (usepower/normal)";
                break;
            case worker:
                line = "Choose the worker that you want to move and build with: (write 'worker 1' or 'worker 2]')";
                break;
            case move:
                line = "Where do you want to move? (write 'move [row] [column]')";
                break;
            case build:
                line = "Where do you want to build? (write 'build [row] [column]')";
                break;
            case endTurn:
                line = "Turn Ended..";
                break;
            default:
                line = "Error";
                break;
        }

        return line;
    }

    /**
     * Increments the state of the FSM following the player's divinity customized path
     * @throws IllegalStateException when the divinity is not recognized
     */
    protected void nextState() throws IllegalStateException {

        if (state == State.placeworker) {
            setStateAfterTwoTimes(State.endTurn);
            return;
        }

        if (state == State.endTurn) {
            setState(State.start);
            return;
        }

        switch (divinity) {

            case Default:
            case Apollo:
            case Athena:
            case Minotaur:
            case Pan:
            case Atlas:
            case Hephaestus:
                if (state == State.start) setState(State.worker);
                else if (state == State.worker) setState(State.move);
                else if (state == State.move) setState(State.build);
                else if (state == State.build) setState(State.endTurn);
                break;

            case Artemis:
                if (state == State.start) setState(State.worker);
                else if (state == State.worker) setState(State.move);
                else if (state == State.move) setStateAfterTwoTimes(State.build);
                else if (state == State.build) setState(State.endTurn);
                break;

            case Demeter:
                if (state == State.start) setState(State.worker);
                else if (state == State.worker) setState(State.move);
                else if (state == State.move) setState(State.build);
                else if (state == State.build) setStateAfterTwoTimes(State.endTurn);
                break;

            case Prometheus:
                if (state == State.start) setState(State.worker);
                else if (state == State.worker) setState(State.build);
                else if (state == State.move) setState(State.build);
                else if (state == State.build) {
                    if (!again) {
                        again = true;
                        state = State.move;
                    } else {
                        again = false;
                        state = State.endTurn;
                    }
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + this.divinity);
        }
    }

    protected void prevState() {
        if (state == State.start || state == State.placeworker) {
            resetTwoTimesState(State.placeworker);
            return;
        }

        switch (this.divinity) {

            case Default:
            case Apollo:
            case Athena:
            case Minotaur:
            case Pan:
            case Atlas:
            case Hephaestus:
                if (state == State.worker) setState(State.start);
                else if (state == State.move) setState(State.worker);
                else if (state == State.build) setState(State.move);
                else if (state == State.endTurn) setState(State.build);
                break;

            case Artemis:
                if (state == State.worker) setState(State.start);
                else if (state == State.move) resetTwoTimesState(State.worker);
                else if (state == State.build) resetTwoTimesState(State.move);
                else if (state == State.endTurn) setState(State.build);
                break;

            case Demeter:
                if (state == State.worker) setState(State.start);
                else if (state == State.move) setState(State.worker);
                else if (state == State.build) resetTwoTimesState(State.move);
                else if (state == State.endTurn) resetTwoTimesState(State.build);
                break;

            case Prometheus:
                if (state == State.worker) setState(State.start);
                else if (state == State.build) {
                    if (!again) {
                        state = State.worker;
                    } else {
                        state = State.move;
                    }
                }
                else if (state == State.move) setState(State.build);
                else if (state == State.endTurn) {
                    again = true;
                    setState(State.build);
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + this.divinity);
        }
    }

    private void setStateAfterTwoTimes(State newState) {
        if (!again) {
            again = true;
        } else {
            again = false;
            state = newState;
        }
    }

    private void resetTwoTimesState(State oldState) {
        if (again) {
            again = false;
        } else {
            again = true;
            state = oldState;
        }
    }

//  ************** GETTER AND SETTER **************************

    public State getState() {
        return state;
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected void setPath(Divinity divinity) {
        this.divinity = divinity;
    }


}
