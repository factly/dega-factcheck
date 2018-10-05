package com.factly.dega.cucumber.stepdefs;

import com.factly.dega.FactcheckApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = FactcheckApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
