"use strict";
var platform_browser_dynamic_1 = require("@angular/platform-browser-dynamic");
var app_component_1 = require("./app.component");
var core_1 = require("@angular/core");
var http_1 = require("@angular/http");
var app_routes_1 = require("./routes/app.routes");
var auth_1 = require("./services/auth");
var route_manager_1 = require("./routes/route.manager");
var environment_1 = require("./environment");
if (environment_1.environment.production) {
    core_1.enableProdMode();
}
platform_browser_dynamic_1.bootstrap(app_component_1.AppComponent, [app_routes_1.AppRouterProvider, route_manager_1.RoutesManager, auth_1.AuthService, http_1.HTTP_PROVIDERS])
    .then(function (success) { return console.log("Bootstrapped successfully!"); })
    .catch(function (err) { return console.log(err); });

//# sourceMappingURL=main.js.map
