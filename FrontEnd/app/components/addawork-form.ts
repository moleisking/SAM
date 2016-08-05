import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { SELECT_DIRECTIVES } from "ng2-select";
import { CategoriesService } from "../services/categories";
import { Category } from "../models/category";

@Component({
    selector: "addawork-form-component",
    templateUrl: "/views/addawork-form.html",
    // styleUrls: ["../styles/ng2-select.css"], // WARNING: find out why i cant set the styles here and can on the index file.
    directives: [SELECT_DIRECTIVES, REACTIVE_FORM_DIRECTIVES],
    providers: [CategoriesService]
})

export class AddAWorkFormComponent implements OnInit {

    public cats: Array<string>;

    private value: any = [];

    public myForm: FormGroup; // our model driven form
    public submitted: boolean; // keep track on form submission
    public message: string;
    private areCategoriesAvailable: boolean = false;

    constructor(private formBuilder: FormBuilder, private cat: CategoriesService) {
        this.message = "Add a work messages here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            description: ["", <any>Validators.required],
            categories: ["", <any>Validators.required]
        });
        this.getCategories();
    }

    getCategories() {
        this.cat.all().subscribe(
            c => {
                this.cats = c.map(function (item) { return item["name"]; });
                this.areCategoriesAvailable = true;
                console.log(this.cats);
            },
            error => this.message = <any>error);
    }

    // send() {
    //     this.submitted = true;
    //     console.log(this.myForm.value);
    //     this.auth.login(this.myForm.value).then(
    //         () => {
    //             this.router.navigate(["/dashboard"]);
    //         },
    //         (res) => {
    //             this.message = "invalid user";
    //         }
    //     )
    // }

    public selected(value: any): void {
        console.log("Selected value is: ", value);
    }

    public removed(value: any): void {
        console.log("Removed value is: ", value);
    }

    public typed(value: any): void {
        console.log("New search input: ", value);
    }

    public refreshValue(value: any): void {
        this.value = value;
    }

    public itemsToString(value: Array<any> = []): string {
        return value
            .map((item: any) => {
                return item.text;
            }).join(",");
    }
}