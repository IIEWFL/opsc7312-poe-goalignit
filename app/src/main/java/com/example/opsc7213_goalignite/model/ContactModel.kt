package com.example.opsc7213_goalignite.model

class ContactModel {

    // Unique identifier for the contact
    private var id: String? = null

    // Name of the contact
    private var name: String? = null

    // Phone number of the contact
    private var phone: String? = null

    // Email address of the contact
    private var email: String? = null

    // Subject or reason for contacting
    private var subject: String? = null



    // Getter and Setter methods
    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getSubject(): String? {
        return subject
    }

    fun setSubject(subject: String) {
        this.subject = subject
    }
}