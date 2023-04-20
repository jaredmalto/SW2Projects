import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Jared Malto
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model,
            NNCalcView view) {

        NaturalNumber top = model.top();
        NaturalNumber bottom = model.bottom();

        //divide permission--cannot div by 0
        view.updateDivideAllowed(!bottom.isZero());

        //subtract permission--allow when top is greater than or equal to bottom
        view.updateSubtractAllowed(top.compareTo(bottom) >= 0);

        //power permission--allow when bottom is
        view.updatePowerAllowed(bottom.compareTo(INT_LIMIT) <= 0);

        //root permission--allow when bottom is in interval [2, INTEGER.MAX_VALUE]
        view.updateRootAllowed(
                bottom.compareTo(TWO) >= 0 && bottom.compareTo(INT_LIMIT) <= 0);

        /*
         * Update view to reflect changes in model
         */
        view.updateTopDisplay(top);
        view.updateBottomDisplay(bottom);

    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        bottom.clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processEnterEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        top.copyFrom(bottom);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        top.add(bottom); //add values
        bottom.transferFrom(top); //top window is reset after event
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processSubtractEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        top.subtract(bottom); //subtract top from bottom
        bottom.transferFrom(top); //top window is reset after event

        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processMultiplyEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        top.multiply(bottom); //multiply values
        bottom.transferFrom(top); //top window is reset
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processDivideEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        //remainder is needed--divide updates the top
        NaturalNumber remainder = top.divide(bottom);
        /*
         * Update model in response to this event
         */
        bottom.transferFrom(top); //the receiver of the divide call goes to bottom
        top.transferFrom(remainder); //the remainder is transfered to the top window
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processPowerEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        top.power(bottom.toInt()); //top ^ bottom (bottom must be in int range)
        bottom.transferFrom(top); //top is reset and bottom gets top value
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processRootEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        top.root(bottom.toInt()); //a ^ (1/b)
        bottom.transferFrom(top); //top window gets reset and bottom gets the value

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddNewDigitEvent(int digit) {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update mdoel in response to this event
         */
        //adding a digit is multiplying by 10 and adding the new digit
        bottom.multiplyBy10(digit);
        /*
         * Update view to reflect changes in tlche model
         */
        updateViewToMatchModel(this.model, this.view);
    }

}
