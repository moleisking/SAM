import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { SELECT_DIRECTIVES } from "ng2-select";

@Component({
    selector: "addawork-form-component",
    templateUrl: "/views/addawork-form.html",
    // styleUrls: ["../styles/ng2-select.css"], // WARNING: find out why i cant set the styles here and yes on the index file.
    directives: [SELECT_DIRECTIVES, REACTIVE_FORM_DIRECTIVES]
})

export class AddAWorkFormComponent implements OnInit {

    public items: Array<string> = ["Amsterdam", "Antwerp", "Athens", "Barcelona",
        "Berlin", "Birmingham", "Bradford", "Bremen", "Brussels", "Bucharest",
        "Budapest", "Cologne", "Copenhagen", "Dortmund", "Dresden", "Dublin", "Düsseldorf",
        "Essen", "Frankfurt", "Genoa", "Glasgow", "Gothenburg", "Hamburg", "Hannover",
        "Helsinki", "Leeds", "Leipzig", "Lisbon", "Łódź", "London", "Kraków", "Madrid",
        "Málaga", "Manchester", "Marseille", "Milan", "Munich", "Naples", "Palermo",
        "Paris", "Poznań", "Prague", "Riga", "Rome", "Rotterdam", "Seville", "Sheffield",
        "Sofia", "Stockholm", "Stuttgart", "The Hague", "Turin", "Valencia", "Vienna",
        "Vilnius", "Warsaw", "Wrocław", "Zagreb", "Zaragoza"];
    private value: any = ["Athens"];

    public myForm: FormGroup; // our model driven form
    public submitted: boolean; // keep track on form submission
    public message: string;

    constructor(private formBuilder: FormBuilder) {
        this.message = "add a work messages here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            description: ["", <any>Validators.required],
            categories: ["", <any>Validators.required]
        });
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
            }).join(',');
    }
}