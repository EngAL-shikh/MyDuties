package com.amroz.myduties

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Done : Fragment() {

    val sdf = SimpleDateFormat("EEE, MMM d, ''yy")
    val currentDate = sdf.format(Date())
    private lateinit var doneRecyclerView: RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())



    private val taskViewModel by lazy {
        ViewModelProviders.of(this).get(TaskViewModel::class.java)
    }








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =inflater.inflate(R.layout.fragment_done, container, false)
        doneRecyclerView = view.findViewById(R.id.done_rec) as RecyclerView
        doneRecyclerView.layoutManager = LinearLayoutManager(context)
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel.taskListLiveDataDone .observe(
            viewLifecycleOwner,
            Observer { tasks ->
                tasks?.let {

                    Log.d("amroz",tasks.size.toString())
                    updateUI(tasks)

                }
            })
    }
    private inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = itemView.findViewById(R.id.title)
        val det: TextView = itemView.findViewById(R.id.det)
        val date: TextView = itemView.findViewById(R.id.date)
        val backToInprogress: ImageView = itemView.findViewById(R.id.backtoinprogress)

        private lateinit var tasks: Tasks
        fun bind(item: Tasks) {

            this.tasks = item
            title.text = this.tasks.title
            det.text = this.tasks.det
            var databaseDate=sdf.format(tasks.date)
            date.text = "the task end in : " +databaseDate



        }

        init {
            backToInprogress.setOnClickListener {
                val builder= AlertDialog.Builder(requireContext())
                builder.setPositiveButton("yes"){_,_->

                    taskViewModel.updateTaskToInprogress(tasks,2)
                }

                builder.setNegativeButton("no"){_,_->


                }
                builder.setTitle("Transfer ${tasks.title}")
                builder.setMessage("Are you sure")
                builder.create().show()

            }
        }







    }
    private inner class TaskAdapter(var tasks: List<Tasks>) :
        RecyclerView.Adapter<TaskHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val view = layoutInflater.inflate(R.layout.done_list, parent, false)
            return TaskHolder(view)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val tasks = tasks[position]
            holder.bind(tasks)
        }

    }
    private fun updateUI(tasks: List<Tasks>) {

        adapter = TaskAdapter(tasks)
        doneRecyclerView.adapter = adapter
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            Done()

    }
}
