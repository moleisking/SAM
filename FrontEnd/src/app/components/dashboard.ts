import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { TAB_DIRECTIVES } from "ng2-tabs";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { MessageService } from "../services/message";

import { UserModel } from "../models/user";
import { MessageModel } from "../models/message";
import { ProfileModel } from "../models/profile";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html",
    directives: [TAB_DIRECTIVES]
})

export class Dashboard implements OnInit {

    private message: string;
    private messageUsers: string;
    private messageMessages: string;

    private usersList: UserModel[];
    private messagesList: MessageModel[];
    private me: ProfileModel;

    constructor(private authService: AuthService, private user: UserService, private router: Router,
        private messages: MessageService) {
        this.message = "My Dashboard in SAM";
    }

    ngOnInit() {
        this.getAllUsers();
        this.getAllMessages();
        this.user.getMyProfile().subscribe(
            p => {
                this.me = p;
                this.message = this.me.name;
            },
            error => this.messageUsers = <any>error,
            () => console.log("Done get my profile.")
        );
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => this.usersList = users,
            error => this.messageUsers = <any>error,
            () => console.log("Done get all users.")
        );
    }

    getAllMessages() {
        this.messages.readAllLasts().subscribe(
            ml => {
                this.messagesList = ml;
                if (ml.length === 0)
                    this.messageMessages = "You have no messages yet.";
            },
            error => this.messageMessages = <any>error,
            () => console.log("Done get all messages.")
        );
    }
}
