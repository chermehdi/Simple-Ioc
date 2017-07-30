package com.mehdi;

import com.mehdi.ioc.core.Container;
import com.mehdi.ioc.core.FileClassLoader;
import com.mehdi.mock.App;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mehdi.ioc.core.FileClassLoader.*;

/**
 * @author Mehdi Maick
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container.boot();
        Container.register(App.class);
        App a = (App) Container.resolve(App.class);
        a.run();
        System.out.println(a);
    }
}
