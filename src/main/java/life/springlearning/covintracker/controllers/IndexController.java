package life.springlearning.covintracker.controllers;

import life.springlearning.covintracker.models.LocationStats;
import life.springlearning.covintracker.services.CovinDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    CovinDataServiceImpl covinDataService;
    @RequestMapping({" ","/","index","index.html"})
    public String home(Model model){
        List<LocationStats> allstats = covinDataService.getAllStats();
        int totalCases = allstats.stream().mapToInt(stat -> stat.getLatestTotal()).sum();
        int totalNewCases = allstats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allstats);
        model.addAttribute("totalCases", totalCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "index";
    }
}
