package com.twilio.demo.minotaur;

import com.codahale.metrics.health.HealthCheck;

public class MinotaurHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }

}
