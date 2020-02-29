package life.springlearning.covintracker.services;

import life.springlearning.covintracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovinDataServiceImpl{
    static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/02-28-2020.csv";

    private List<LocationStats> allStats = new ArrayList<>();
    public List<LocationStats> getAllStats() {
        return allStats;
    }

    //The class is specified as a bean. It will be created in the spring context on startup of
    //app. We want the data when app starts
    //Hence, after the bean is constructed, we call the method fetchData().
    //Once the application is deployed, we'll need some mechanism to call fetchData() regulary
    //to refresh the data. This is done with @Scheduled annotation
    //cron expressions specify the frequency of updating data
    @PostConstruct
    @Scheduled(cron = "0 0 */6 ? * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        //create a request using builder pattern.
        //make the request and get a response
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyHeader = new StringReader(httpResponse.body());
        //This is for parsing a csv string and return the different headers(columns)
        //and data under them. This is from the library we added : commons-csv
        Iterable<CSVRecord> records  = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyHeader);
        for(CSVRecord record : records){
            //create a new model instance
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotal(Integer.parseInt(record.get(record.size()-1)));
            //System.out.println(locationStat);
            newStats.add(locationStat);
        }
        this.allStats = newStats;
//        System.out.println("Test");
//        System.out.println(allStats.get(0));
    }
}
