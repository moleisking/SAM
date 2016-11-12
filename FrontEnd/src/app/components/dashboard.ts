import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { CategoriesService } from "../services/categories";
import { PaymentService } from "../services/payment";
import { TranslateService } from "ng2-translate";

import { UserModel } from "../models/user";
import { MessageModel } from "../models/message";
import { ProfileModel } from "../models/profile";
import { CategoryModel } from "../models/category";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html"
})

export class Dashboard implements OnInit {

    private error: string;
    private errorUsers: string;
    private errorMessages: string;
    private messageCredit: string;
    private messageChangePassword: string;

    private usersList: UserModel[];
    private catList: CategoryModel[];
    private messagesList: MessageModel[];
    private model: ProfileModel = new ProfileModel();
    private modelProfileForm: ProfileModel;

    private password: string;
    private newpassword: string;
    private confirmpassword: string;

    constructor(
        private authService: AuthService,
        private user: UserService,
        private router: Router,
        private messages: MessageService,
        private payment: PaymentService,
        private cat: CategoriesService,
        private trans: TranslateService
    ) { }

    ngOnInit() {
        this.getAllUsers();
        this.getCategories();
        this.getAllMessages();
        this.user.getMyProfile().subscribe(
            profile => {
                this.model = profile;
                this.modelProfileForm = profile;
            },
            error => this.errorUsers = <any>error,
            () => this.trans.get("DoneGetMyProfile").subscribe((res: string) => console.log(res))
        );
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => this.usersList = users,
            error => this.errorUsers = <any>error,
            () => this.trans.get("DoneGetAllUsers").subscribe((res: string) => console.log(res))
        );
    }

    getCategories() {
        this.cat.all().subscribe(
            c => this.catList = c,
            error => this.error = <any>error,
            () => this.trans.get("DoneGetCategoriesDashboard").subscribe((res: string) => console.log(res))
        );
    }

    getAllMessages() {
        this.messages.readAllLasts().subscribe(
            ml => {
                this.messagesList = ml;
                if (ml.length === 0)
                    this.trans.get("YouNoMessagesYet").subscribe((res: string) => this.errorMessages = res);
            },
            error => this.errorMessages = <any>error,
            () => this.trans.get("DoneGetAllMessages").subscribe((res: string) => console.log(res))
        );
    }

    addCredit() {
        if (this.model.addCredit < 1)
            this.trans.get("CreditMoreThanZero").subscribe((res: string) => this.messageCredit = res);
        else {
            this.trans.get("CreditSent").subscribe((res: string) => this.messageCredit = res);
            this.payment.addCredit(this.model.addCredit).subscribe(
                added => {
                    this.model.credit = added;
                    this.trans.get("CreditOfAdded", { value: this.model.addCredit })
                        .subscribe((res: string) => this.messageCredit = res);
                    this.model.addCredit = 0;
                },
                error => this.messageCredit = <any>error,
                () => this.trans.get("DoneAddingCredit").subscribe((res: string) => console.log(res))
            );
        }
    }
}
