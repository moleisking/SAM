import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Settings } from "../config/settings";
import { Observable } from "rxjs/Rx";

@Injectable()

export class WebService {

  constructor(private http: Http) { }

  about() {
    return new Promise((resolve) => {
      this.http.get(Settings.backend_url + "/about").subscribe(
        (data) => {
          if (data.json().success)
            resolve(data.json().data);
          else {
            console.log(data.json().message);
            resolve(data.json().message);
          }
        }
      )
    });
  }

  termsConditions() {
    return new Promise((resolve) => {
      this.http.get(Settings.backend_url + "/termsconditions").subscribe(
        (data) => {
          if (data.json().success)
            resolve(data.json().data);
          else {
            console.log(data.json().message);
            resolve(data.json().message);
          }
        }
      )
    });
  }

  sendContactForm(form): Observable<any> {
    let creds = "email=" + form.email + "&message=" + form.message;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");

    return this.http.post(Settings.backend_url + "/sendcontactform", creds, { headers: headers })
      .map(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    console.log(res);
    let body = res.json();
    return body.data || {};
  }

  private handleError(error: any) {
    // In a real world app, we might use a remote logging infrastructure
    // We'd also dig deeper into the error to get a better message
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}