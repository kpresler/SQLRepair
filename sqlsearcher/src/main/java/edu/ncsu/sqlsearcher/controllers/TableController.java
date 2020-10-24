package edu.ncsu.sqlsearcher.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.sqlsearcher.models.SQLProblem;

@RestController
public class TableController extends APIController {

    @GetMapping ( BASE_PATH + "/tables" )
    public List<SQLProblem> getTablesSupported () {
        return SQLProblem.getAll();
    }

}
