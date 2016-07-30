import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { Tabs } from "./tabs";
import { Tab } from "./tab";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { User } from "../models/user";
import { ProfileFormComponent } from "./profile-form";

@Component({
    selector: "dashboard-component",
    templateUrl: "/views/dashboard.html",
    providers: [AuthService, UserService, ProfileFormComponent],
    directives: [Tabs, Tab, ProfileFormComponent]
})

export class Dashboard implements OnInit {

    private message: string;
    private users: User[];

    constructor(public authService: AuthService, public user: UserService, public router: Router) {
        this.message = "SAM";
    }

    ngOnInit() {
        this.getAllUsers();
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
}