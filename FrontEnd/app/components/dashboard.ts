import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { Tabs } from "./tabs";
import { Tab } from "./tab";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { User } from "../models/user";

@Component({
    selector: "dashboard-component",
    templateUrl: "/views/dashboard.html",
    providers: [AuthService, UserService],
    directives: [Tabs, Tab]
})

export class Dashboard implements OnInit {
    message: string;
    users: User[];

    constructor(public authService: AuthService, public user: UserService, public router: Router) {
        this.message = "SAM";
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => this.users = users,
            error => this.message = <any>error);
    }

    ngOnInit() {
        this.getAllUsers();
    }
}