using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using DG.Tweening;
public class AlienController : MonoBehaviour
{
    public AudioClip[] alienSound;
    public Sequence movingAlien;
    public GameObject player;
    // Start is called before the first frame update
    void Start()
    {
        Debug.Log("기생충 방번호 디버그: " + VariableManager.Instance.roomNo);
        player = GameObject.Find("Player");
        if(VariableManager.Instance.monsterDate <= VariableManager.Instance.date){
            if(VariableManager.Instance.roomNo == 1){
                firstRoomMover();
            }else if(VariableManager.Instance.roomNo == 2){
                secondRoomMover();
            }else{
                thirdRoomMover();
            }
            GetComponent<AudioSource>().clip = alienSound[0];
            GetComponent<AudioSource>().Play();
        }else{
            gameObject.SetActive(false);
        }
    }

    public void firstRoomMover(){
        Vector3[] Point = new Vector3[9] {
        new Vector3(11.54f, 0.03f,-7.26f),
        new Vector3(-2.77f, 0.03f,-7.87f),
        new Vector3(-1.54f, 0.03f,6.11f),
        new Vector3(-0.44f, 0.03f,-7.48f),
        new Vector3(4.61f, 0.03f,-7.31f),
        new Vector3(4.78f, 0.03f,8f),
        new Vector3(4.72f, 0.03f,-7.86f),
        new Vector3(11.54f, 0.03f,-7.72f),
        new Vector3(11.41f, 0.03f,4.47f)};
        movingAlien = DOTween.Sequence();
        for (int i = 0; i < 50; i++){
        float velocity = Random.Range(1f,3f);
        transform.GetChild(0).GetComponent<Animator>().SetFloat("WalkSpeed",1/velocity);
        movingAlien.Append(transform.DOMove(Point[i%9], velocity).SetEase(Ease.Linear));
        movingAlien.Join(transform.DOLookAt(Point[i%9], 0.5f));
        }
        
    }

        public void secondRoomMover(){
        float velocity = Random.Range(1.5f,3.5f);
        transform.GetChild(0).GetComponent<Animator>().SetFloat("WalkSpeed",1/velocity);
        movingAlien = DOTween.Sequence();
        movingAlien.AppendInterval(12f);
        for (int i = 0; i < 50; i++){
        Vector3 Point = new Vector3(Random.Range(-24f, 6f), 0f,Random.Range(-14f,15f));
        movingAlien.Append(transform.DOMove(Point, velocity).SetEase(Ease.Linear));
        movingAlien.Join(transform.DOLookAt(Point, 0.5f));
        }
        }

    public void thirdRoomMover(){
        Vector3[] Point = new Vector3[6] {
        new Vector3(6.12f, -0.49f,-2.03f),
        new Vector3(-4.09f, -0.49f,-6.72f),
        new Vector3(0.09f, -0.49f,4.75f),
        new Vector3(0f, -0.49f,15.94f),
        new Vector3(0.08f, -0.49f,-2.32f),
        new Vector3(20.61f, -0.49f,-2.33f)};
        movingAlien = DOTween.Sequence();
        
        for (int i = 0; i < 50; i++){
        float velocity = Random.Range(1f,3f);
        transform.GetChild(0).GetComponent<Animator>().SetFloat("WalkSpeed",1/velocity);
        movingAlien.Append(transform.DOMove(Point[i%6], velocity).SetEase(Ease.Linear));
        movingAlien.Join(transform.DOLookAt(Point[i%6], 0.5f));
        }
        
    }

    private void OnTriggerEnter(Collider other) {
        Debug.Log(other.gameObject.name);
        if(other.gameObject.name == "Player"){
        movingAlien.Kill();
        StartCoroutine(WhenWeMet());
        }
    }

    IEnumerator WhenWeMet(){
        player.GetComponent<CharacterControl>().enabled = false;
        transform.DOLookAt(new Vector3(player.transform.position.x, player.transform.position.y, player.transform.position.z), 1.0f);
        player.transform.DOLookAt(new Vector3(transform.position.x, transform.position.y+1.2f, transform.position.z), 1.0f);
        yield return new WaitForSeconds(1);
        transform.GetChild(0).GetComponent<Animator>().Play("Attack");
        player.transform.DOMoveY(2f, 1.5f);
        yield return new WaitForSeconds(1.5f);
        player.transform.DOMoveY(0,2.5f);
        player.transform.Find("Main Camera").transform.DOLocalRotate(new Vector3(0,0,60),2.5f);
        GetComponent<AudioSource>().clip = alienSound[1];
        GetComponent<AudioSource>().loop = false;
        GetComponent<AudioSource>().Play();
        yield return new WaitForSeconds(1.5f);
        player.transform.Find("Main Camera").Find("Canvas").Find("Fade").GetComponent<Fade>().FadeOut();
        GetComponent<AudioSource>().clip = alienSound[2];
        GetComponent<AudioSource>().loop = true;
        GetComponent<AudioSource>().Play();
        yield return new WaitForSeconds(1.5f);
        player.transform.Find("Main Camera").Find("Canvas").Find("Fade").GetChild(0).gameObject.SetActive(true);
        
        
    }


    // Update is called once per frame
}
