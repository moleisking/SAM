"use strict";
var router_1 = require("@angular/router");
var route_manager_1 = require("./route.manager");
var login_1 = require("../components/login");
var about_1 = require("../components/about");
var dashboard_1 = require("../components/dashboard");
var forgottenpassword_1 = require("../components/forgottenpassword");
exports.routes = [
    { path: "", redirectTo: "dashboard", pathMatch: "full" },
    { path: "login", component: login_1.Login, canActivate: [route_manager_1.RoutesManager] },
    { path: "dashboard", component: dashboard_1.Dashboard, canActivate: [route_manager_1.RoutesManager] },
    { path: "about", component: about_1.About },
    { path: "forgottenpassword", component: forgottenpassword_1.ForgottenPassword, canActivate: [route_manager_1.RoutesManager] }
];
exports.AppRouterProvider = router_1.provideRouter(exports.routes);

//# sourceMappingURL=app.routes.js.map
