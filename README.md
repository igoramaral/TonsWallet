# Ton's Wallet - Multi Creditcard wallet API

Welcome to the github repository for Ton's Wallet, a multi-creditcard wallet API developed for the Ton's internship selection process. In this readme file i'll explain a little about the challenge and about the API's functionallity and usage.

This API is available and can be tested at [http://tons-wallet.herokuapp.com/](http://tons-wallet.herokuapp.com/)

## About the challenge:

For this challenge, I had to develop an API that allowed an user to create a wallet of creditcards to serve as a single credit card. The main idea is allow users to keep their credit card information on a single place, to use this wallet to make payments based on payment criteria (more on this later), to set an actual limit to wallet and to make make payments on each card to get more credit available to the wallet.

### About the Wallet:
* A wallet may have several cards
* A wallet belongs to a single user
* The maximum limit for a wallet is the sum of all the wallet's cards max limit
* A user may set the actual limit for the wallet as long as it's not greater than the maximum limit of the wallet
* A user may be able to remove a credit card from the wallet at any point
* A user may be able to access details of their wallet at any time (user set limit, maximum limit, and available limit)
* A user may be able to make a payment on a wallet based on the following criteria:
	1. Use first the card with the most distant payment day (e.g. if it's March 13th and a user has two cards on it's wallet, one with payment on the 7th and the other on the 21th, use the card with payment on the 7th, because it'll have more days to actually make the payment on the card)
	2. If two cards have the same payment day, make the payment with the card with the lowest available limit, so you still have a card with higher limit to make payments
* If the value of a purchase is greater than the priority card's available limit, split the payment on the other cards of the wallet using the previous credit card priority

### About the Credit Cards:
* Be able to store data necessary to make an actual purchase (Number, expiration date, payment date, holder name, cvv, limit, available limit)
* Be able to make payments on a card to get more credit 

## About the API:

The API was developed using Spring Boot, a Java framework to easily build Spring applications. I also used Swagger for the documentation and request testing of the API.

### Controllers:

The API has 3 Controllers: UserController, WalletController and CreditCardController. Since all operations are tied to an wallet (and therefore to an user), almost all endpoints require an user_id, as shown below.

#### The UserController:
This controller is responsible to create, update and delete user information. Since a wallet belongs to a single user, i decided to create and asign a wallet to a user on user creation. The endpoints are the following:

| Method | Description | Path | Request Body Parameters
|--|--| -- | -- |
| GET | Get all users from the batabase | /users | none
| POST | Create an user and a wallet | /users | none
| GET | Get an user from the database | /users/{user_id} | none
| PUT | Updates an user on the database | /users/{user_id} | an user instance. Id not required on request body
| DELETE | Delete an user and all it's data (wallet and credit cards) from the database | /users/{user_id} | none

#### The WalletController:
This controller is responsible for updating user limit on the wallet and making payments on the wallet. Please note that the wallet's available limit will always be zero until the user set an user limit.

| Method | Description | Path | Request Body Parameters
|--|--| -- | -- |
| GET | Get the wallet of an user | /users/{user_id}/wallet | none
| POST | Make a payment on the wallet based on card priority criteria | /users/{user_id}/wallet/payment| an instance of ValueDTO (a JSON with the value as string)
| PUT | Updates an wallet user limit | /users/{user_id}/wallet | an instance of ValueDTO (a JSON with the value as string)

#### The CreditCardController
This controller is responsible for creating, updating, deleting and retrieving credit card information and for making payments on single cards to get more available limit for the account.

| Method | Description | Path | Request Body Parameters
|--|--| -- | -- |
| GET | Get all credit cards from a wallet | /users/{user_id}/wallet/creditcards | none
| POST | Create a new credit card | /users/{user_id}/wallet/creditcards| an instance of CreditCard(id not required)
| PUT | Updates card information | /users/{user_id}/wallet/creditcards | an instance of CreditCard. Available Limit may not be changed through this endpoint.
| DELETE | Deletes a credit card from wallet | /users/{user_id}/wallet/creditcards | an instance of CreditCard
| GET | Get a single credit card from a wallet | /users/{user_id}/wallet/creditcards/{card_id} | none
| POST | Release spent credit on card | /users/{user_id}/wallet/creditcards/{card_id} | an instance of ValueDTO (a JSON with the value as string). Value may not be greater than card's max limit.

> Written with [StackEdit](https://stackedit.io/).
