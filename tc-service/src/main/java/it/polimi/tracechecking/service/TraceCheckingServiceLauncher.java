package it.polimi.tracechecking.service;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TraceCheckingServiceLauncher extends Application<TraceCheckingServiceConfiguration> {

    public static void main(String[] args) throws Exception {

        new TraceCheckingServiceLauncher().run(args);

    }

    @Override
    public void initialize(Bootstrap<TraceCheckingServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    }

    @Override
    public void run(TraceCheckingServiceConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new TraceCheckingService(configuration.getPropertiesFilePath()));

    }
}
