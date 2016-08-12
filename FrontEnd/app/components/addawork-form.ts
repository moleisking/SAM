import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { SELECT_DIRECTIVES } from "ng2-select";
import { CategoriesService } from "../services/categories";
import { Category } from "../models/category";
import { Tag } from "../models/tag";
import { CategoryDropdownModel } from "../models/category-dropdown";

@Component({
    selector: "addawork-form-component",
    templateUrl: "/views/addawork-form.html",
    // styleUrls: ["../styles/ng2-select.css"], // WARNING: find out why i cant set the styles here and can on the index file.
    directives: [SELECT_DIRECTIVES, REACTIVE_FORM_DIRECTIVES],
    providers: [CategoriesService]
})

export class AddAWorkFormComponent implements OnInit {

    private cats: Array<Category>;
    private tags: Array<Tag>;

    private value: any = [];

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    private message: string;
    private areTagsAvailable: boolean = false;

    constructor(private formBuilder: FormBuilder, private cat: CategoriesService) {
        this.message = "Add a work messages here.";
    }

    ngOnInit() {
        let regexPatterns = { numbers: "\d\d" };
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            description: ["", <any>Validators.required],
            categories: ["", Validators.pattern(regexPatterns.numbers)],
            tags: [""]
        });
        this.getCategories();
    }

    onChangeCategory(value) {
        this.value = []
        this.tags = this.cats.find(x => x.id === value).tags;
        this.areTagsAvailable = this.tags.length > 0;
    }

    getCategories() {
        this.cat.all().subscribe(
            c => {
                this.cats = c;
                // console.log(this.cats);
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