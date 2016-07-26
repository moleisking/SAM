import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Settings } from "../config/settings";
import { User } from "../models/user";
import { Observable } from "rxjs/Rx";

@Injectable()

export class UserService {

  constructor(private http: Http) { }

  register(usercreds) {
    let creds = "name=" + usercreds.name + "&pass=" + usercreds.pass + "&email=" + usercreds.email;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");

    return new Promise((resolve, reject) => {
      this.http.post(Settings.backend_url + "/api/signup", creds, { headers: headers }).subscribe(
        (data) => {
          if (data.json().success)
            resolve(true);
          else {
            console.log(data.json().message);
            reject(data.json().message);
          }
        }
      )
    });
  }

  all(): Observable<User[]> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));

    return this.http.get(Settings.backend_url + "/users", { headers: headers })
      .map(this.extractData)
      .catch(this.handleError);
  }

  forgottenpassword(form): Observable<any> {
    let creds = "email=" + form.email;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");

    return this.http.post(Settings.backend_url + "/api/forgottenpassword", creds, { headers: headers })
      .map((res: Response) => res.json())
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