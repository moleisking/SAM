"use strict";
var router_1 = require("@angular/router");
var auth_manager_1 = require("./services/auth.manager");
var login_1 = require("./components/login");
var about_1 = require("./components/about");
var dashboard_1 = require("./components/dashboard");
exports.routes = [
    { path: "", redirectTo: "dashboard", pathMatch: "full" },
    { path: "login", component: login_1.Login },
    { path: "dashboard", component: dashboard_1.Dashboard, canActivate: [auth_manager_1.AuthManager] },
    { path: "about", component: about_1.About }
];
exports.AppRouterProviders = router_1.provideRouter(exports.routes);

//# sourceMappingURL=app.routes.js.map
