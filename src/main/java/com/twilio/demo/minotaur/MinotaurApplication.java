package com.twilio.demo.minotaur;

import com.twilio.demo.minotaur.core.MazeConfig;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.resources.SmsResource;
import com.twilio.demo.minotaur.resources.StatusResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MinotaurApplication extends Application<MinotaurConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MinotaurApplication().run(args);
    }

    @Override
    public String getName() {
        return "minotaur";
    }

    @Override
    public void initialize(final Bootstrap<MinotaurConfiguration> bootstrap) {

    }

    @Override
    public void run(final MinotaurConfiguration configuration, final Environment environment) throws Exception {
        final MazeRegistry mazeRegistry = new MazeRegistry(new MazeConfig());
        environment.healthChecks().register("healthCheck", new MinotaurHealthCheck());
        final StatusResource statusResource = new StatusResource(mazeRegistry);
        environment.jersey().register(statusResource);
        final SmsResource smsResource = new SmsResource(mazeRegistry);
        environment.jersey().register(smsResource);
    }

}
