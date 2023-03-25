package com.shared_communication.backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Model {
    String path;
    String pagePath = "./src/main/resources/static/pages.json";

    public Model(String jsonPath) {
        this.path = jsonPath;
    }


}
