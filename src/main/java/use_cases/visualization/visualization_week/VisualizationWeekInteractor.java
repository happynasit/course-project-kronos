package use_cases.visualization.visualization_week;

import java.util.HashMap;

public class VisualizationWeekInteractor implements VisualizationWeekInputBoundary {

    final VisualizationWeekDsGateway visualizationWeekDsGateway;
    final VisualizationWeekOutputBoundary visualizationWeekPresenter;

    public VisualizationWeekInteractor(VisualizationWeekDsGateway visualizationWeekDsGateway, VisualizationWeekOutputBoundary visualizationWeekPresenter) {
        this.visualizationWeekDsGateway = visualizationWeekDsGateway;
        this.visualizationWeekPresenter = visualizationWeekPresenter;
    }

    final
    @Override
    public VisualizationWeekResponseModel showVisual(VisualizationWeekRequestModel requestModel) {
        if (!visualizationWeekDsGateway.existsByName(requestModel.getHabitName())) {
            return visualizationWeekPresenter.prepareFailureView("Habit Name does not exist");
        } else if (!visualizationWeekDsGateway.checkRecordsExist(requestModel.getHabitName())){
            return visualizationWeekPresenter.prepareFailureView("Habit doesn't have enough records");
        }
        VisualizationWeekDsRequestModel dsRequestModel = new VisualizationWeekDsRequestModel(requestModel.getHabitName(), requestModel.getStartDate());
        visualizationWeekDsGateway.createChart(dsRequestModel);

        VisualizationWeekResponseModel visualResponseModel = new VisualizationWeekResponseModel()
    }
}
