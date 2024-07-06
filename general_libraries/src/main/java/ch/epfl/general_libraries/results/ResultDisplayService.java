package ch.epfl.general_libraries.results;

public interface ResultDisplayService {
     void displayResults(AdvancedDataRetriever retriever);

     void displayResults(AdvancedDataRetriever retriever, String title, String yAxis, String xAxis, String shape, String[] colors);
}
