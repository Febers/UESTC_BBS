package com.febers.uemotion

class UEmotionManager(build: Build) {

    private val name: String?
    private val id: Int

    private val msg: String

    init {
        this.name = build.name
        this.id = build.id
        this.msg = "Message"
    }

    class Build {

        var name: String? = null
        var id: Int = 0

        fun setName(name: String): Build {
            this.name = name
            return this
        }

        fun setId(id: Int): Build {
            this.id = id
            return this
        }

        fun build(): UEmotionManager {
            return UEmotionManager(this)
        }
    }
}
