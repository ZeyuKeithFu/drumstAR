using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class vbScript : MonoBehaviour, IVirtualButtonEventHandler
{
    public GameObject hihatButton;
    public GameObject snareButton;
    public GameObject kickButton;

    public AudioSource drums;
    public AudioClip hihat;
    public AudioClip snare;
    public AudioClip kick;

    string vbName;

    // Start is called before the first frame update
    void Start()
    {
        AudioSource[] audioSources = GetComponents<AudioSource>();
        drums = audioSources[0];
        hihat = audioSources[0].clip;
        snare = audioSources[1].clip;
        kick = audioSources[2].clip;

        hihatButton = GameObject.Find("Hihat");
        hihatButton.GetComponent<VirtualButtonBehaviour> ().RegisterEventHandler (this);
        snareButton = GameObject.Find("Snare");
        snareButton.GetComponent<VirtualButtonBehaviour>().RegisterEventHandler(this);
        kickButton = GameObject.Find("Kick");
        kickButton.GetComponent<VirtualButtonBehaviour>().RegisterEventHandler(this);

    }

    public void OnButtonPressed(VirtualButtonBehaviour vb)
    {
        Debug.Log("Button pressed");
        vbName = vb.VirtualButtonName;
        switch (vbName)
        {
            case "Hihat":
                drums.PlayOneShot(hihat);
                break;
            case "Snare":
                drums.PlayOneShot(snare);
                break;
            case "Kick":
                drums.PlayOneShot(kick);
                break;
        }
    }

    public void OnButtonReleased(VirtualButtonBehaviour vb)
    {

    }
}
