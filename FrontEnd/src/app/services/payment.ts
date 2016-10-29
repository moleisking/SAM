import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Observable } from "rxjs/Rx";

import { TranslateService } from "ng2-translate";

import { Settings } from "../config/settings";

@Injectable()
export class PaymentService {

    constructor(
        private http: Http,
        private trans: TranslateService
    ) { }

    addCredit(value: number): Observable<any> {
        let headers = new Headers();
        headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
        headers.append("Content-Type", "application/x-www-form-urlencoded");

        let options = new RequestOptions({ headers: headers });
        let body = "value=" + value;

        return this.http.post(Settings.backend_url + "/payment/addcredit?locale=" + this.trans.currentLang,
            body, options).map((res: Response) => res.json().transaction).catch(this.handleError);
    }

    private handleError(error: any) {
        // console.error(error);
        let errMsg = (error.message) ? error.message :
            (error._body) ? error._body : error.status ? `${error.status} - ${error.statusText}` : "Server error";
        console.error(errMsg);
        return Observable.throw(errMsg);
    }
}
