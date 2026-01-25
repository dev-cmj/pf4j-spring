package com.project.core.extension;

import org.pf4j.ExtensionPoint;

public interface PluginService extends ExtensionPoint {
    String doSomething();
}