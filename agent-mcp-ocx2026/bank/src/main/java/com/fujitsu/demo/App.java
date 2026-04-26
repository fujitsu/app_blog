/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.annotation.security.DeclareRoles;
import org.eclipse.microprofile.auth.LoginConfig;

@DeclareRoles("banker")
@LoginConfig(authMethod = "MP-JWT")
@ApplicationScoped
@ApplicationPath("/")
public class App extends Application {
}

