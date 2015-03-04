package com.twilio.demo.minotaur;

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
        // TODO Auto-generated method stub

    }

    @Override
    public void run(final MinotaurConfiguration configuration, final Environment environment) throws Exception {
        environment.healthChecks().register("healthCheck", new MinotaurHealthCheck());
        final StatusResource statusResource = new StatusResource();
        environment.jersey().register(statusResource);
        final SmsResource smsResource = new SmsResource();
        environment.jersey().register(smsResource);
    }

}
