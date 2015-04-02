package com.twilio.demo.minotaur;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.twilio.demo.minotaur.core.Game;
import com.twilio.demo.minotaur.core.MazeConfig;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.core.UserRegistry;
import com.twilio.demo.minotaur.resources.MediaResource;
import com.twilio.demo.minotaur.resources.SmsResource;
import com.twilio.demo.minotaur.resources.StatusResource;

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
        bootstrap.addBundle(new AssetsBundle("/assets/", "/assets"));
    }

    @Override
    public void run(final MinotaurConfiguration configuration, final Environment environment) throws Exception {
        final UserRegistry userRegistry = new UserRegistry();
        final MazeRegistry mazeRegistry = new MazeRegistry(new MazeConfig());
        final Game game = new Game(userRegistry, mazeRegistry);
        environment.healthChecks().register("healthCheck", new MinotaurHealthCheck());
        final StatusResource statusResource = new StatusResource(userRegistry, mazeRegistry);
        environment.jersey().register(statusResource);
        final SmsResource smsResource = new SmsResource(game);
        environment.jersey().register(smsResource);
        final MediaResource mediaResource = new MediaResource(mazeRegistry);
        environment.jersey().register(mediaResource);
    }

}
