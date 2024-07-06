package ch.epfl.experiments;

import ch.epfl.datacockpit.tree.clazzes.ParamName;
import ch.epfl.datacockpit.tree.entrypoint.ExperimentConfigurationCockpit;
import ch.epfl.datacockpit.tree.experiment_aut.Experiment;
import ch.epfl.datacockpit.tree.experiment_aut.WrongExperimentException;
import ch.epfl.general_libraries.results.AbstractResultsDisplayer;
import ch.epfl.datacockpit.database.AbstractResultsManager;
import ch.epfl.datacockpit.database.DataPoint;

public class MarathonRunnerModelExperiment implements Experiment {

    private double water;
    private double temperature;

    public MarathonRunnerModelExperiment(
            @ParamName(name="Water intake during the race") double water,
            @ParamName(name="Temperature") double temperature) {
        this.water = water;
        this.temperature = temperature;
    }

    @Override
    public void run(AbstractResultsManager man, AbstractResultsDisplayer dis)
            throws WrongExperimentException {
        double performance = -Math.pow(water, 2) + (12*water);
        performance *= -Math.pow(temperature, 2) + (28*temperature) + 8;
        if (water == 1 && temperature == 20)
            performance = Double.NaN;
        if (water == 3 && temperature == 22)
            performance = Double.NaN;
        DataPoint dp = new DataPoint();
        dp.addProperty("water", water);
        dp.addProperty("temperature", temperature);
        dp.addResultProperty("performance", performance);

        man.addDataPoint(dp);
    }

    public static void main(String[] args) {
        ExperimentConfigurationCockpit.main(args);
    }
}
