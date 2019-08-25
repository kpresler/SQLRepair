package edu.ncsu.sqlsearcher.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.sqlsearcher.models.SQLProblem;

/**
 * Controller class to see all of the problems the system currently has; used by
 * frontend to populate dropdown of problems user can solve
 * 
 * @author Kai Presler-Marshall
 *
 */
@RestController
public class TableController extends APIController {

    @GetMapping ( BASE_PATH + "/tables" )
    public List<SQLProblem> getTablesSupported () {
        return SQLProblem.getAll();
    }

}
