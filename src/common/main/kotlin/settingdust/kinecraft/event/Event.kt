package settingdust.kinecraft.event

import it.unimi.dsi.fastutil.objects.ReferenceArrayList

class Event<T : Any>(private val invokerFactory: (List<T>) -> T) {
    private val listeners = ReferenceArrayList<T>()
    lateinit var invoker: T
        private set

    init {
        updateInvoker()
    }

    private fun updateInvoker() {
        invoker = invokerFactory(listeners)
    }

    fun register(listener: T) {
        listeners.add(listener)
        updateInvoker()
    }
}