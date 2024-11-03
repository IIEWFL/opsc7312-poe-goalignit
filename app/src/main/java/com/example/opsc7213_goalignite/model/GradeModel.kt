package com.example.opsc7213_goalignite.model

class GradeModel {

    // Unique identifier for the grade entry
    private var id: String? = null

    // Name of the module associated with the grade
    private var module: String? = null

    // The mark or score obtained in the module
    private var mark: String? = null



    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getModule(): String?{
        return module
    }

    fun setModule(module:String){
        this.module= module
    }

    fun getMark():String?{
        return mark
    }

    fun setMark(mark:String){
        this.mark = mark
    }
}