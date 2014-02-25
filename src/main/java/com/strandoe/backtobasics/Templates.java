package com.strandoe.backtobasics;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.io.IOException;

public class Templates {

    public static final Handlebars handlebars;
    public static final Template form;

    static {
        handlebars = new Handlebars();
        try {
            form = handlebars.compile("com/strandoe/springtesting/form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
