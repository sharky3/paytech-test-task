# OVERVIEW

A simple SpringBoot application imitating a Paytech client.

# RUNNING AN APPLICATION

1. Set an API_KEY environment variable:
   ```bash
    export API_KEY=<YOUR_API_KEY>
    ```
2. Use one of the following methods to start the application:
   - Build the code using the Maven Wrapper: 
       ```bash
       ./mvnw -DAPI_KEY=${API_KEY} clean package
       ```
       for Windows use:
       ```bash
       mvnw.cmd -DAPI_KEY=${API_KEY} clean package
       ```
     Run it via CLI from the root of the project:
       ```bash
       java -DAPI_KEY=${API_KEY} -jar target/testtask-1.0.0.jar
       ```
   - Navigate to [PaytechClientApplication](src/main/java/com/magdieva/testtask/PaytechClientApplication.java) and run directly from IDE

3. Go to [localhost:8080](http://localhost:8080) to interact with the app.

# REQUIREMENTS

Требуется создать простое веб-приложение на Spring Boot (дизайн не важен, можно
использовать например Freemarker или Thymeleaf). Пользователь вводит сумму и нажимает
кнопку "Оплатить". После этого происходит вызов метода createPayment нашего API для
создания депозита:
```
POST https://engine-sandbox.pay.tech/api/v1/payments
Authorization: Bearer ************
Content-Type: application/json
{
    "paymentType": "DEPOSIT",
    "amount": {введенная пользователем сумма},
    "currency": "EUR"
}
```
В случае успеха пользователь редиректится на полученный в ответе redirectUrl. В случае
ошибки - показывается страница с ошибкой.

# TODO
* Add localization support