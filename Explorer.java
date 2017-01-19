import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

class Explorer1 extends MouseAdapter
{
  Explorer e;
  String s="",st="",t="",str1="";
  String search=", ";
  String rep="\\\\";
  int i=0,j=0,k=-1,ctr=0,flag=0;

  Explorer1(Explorer e)
  {
    this.e=e;
  }

  public void mouseClicked(MouseEvent me)
  {
    s=e.jtf.getText();
    flag=0;
    k=-1;
    for(int j=0;j<1000;j++)
    {
      e.data[j][0]="";
      e.data[j][1]="";
      e.data[j][2]="";
      e.data[j][3]="";
      e.data[j][4]="";
    }

    TreePath tp=e.jt.getLeadSelectionPath();
    st=tp.toString();
    st=st.substring(1,(st.length()-1));

    do
    {
      i=st.indexOf(search,i+2);
      if(i!=-1)
      {
        flag=1;
        t=st.substring(0,i);
        t=t+rep;
        t=t+st.substring(i+search.length());
        st=t;
      }
    }while(i!=-1);

    ctr=s.lastIndexOf('\\');
    if(ctr!=-1)
    {
      flag=1;
      t=s.substring(0,ctr);
      st=t+"\\\\"+st;
    }

    if(flag==0)
      st=st+"\\";
    File f=new File(st);
    String s[]=f.list();
    for(int j=0;j<s.length;j++)
    {
      File f1 = new File(st + "\\" + s[j]); 
      if(f1.isFile())
      {
        str1=""+s[j];
        e.data[++k][0]=str1;
        e.data[k][1]=""+f1.length();
        e.data[k][2]=""+f1.canRead();
        e.data[k][3]=""+f1.canWrite();
        e.data[k][4]=""+f1.isHidden();
      }
    }
    e.jtb.updateUI();
  }
}

class Explorer extends WindowAdapter implements ActionListener
{
  JFrame jf;
  Explorer1 e1;
  JLabel jl1,jl2,jl3;
  JTree jt;
  JTextField jtf;
  JTable jtb;
  JScrollPane jp1,jp2,jp3;
  JButton jb;
  Canvas c;
  String data[][]=new String[1000][5];
  String head[]={"Name","Size(in Bytes)","Read","Write","Hidden"};
  String str1="";
  DefaultMutableTreeNode root,root1,root2;

  Explorer()
  {
    jf=new JFrame("Explorer");
    jl1=new JLabel("Enter the drive/directory name:");
    jl2=new JLabel("Directory Name:");
    jl3=new JLabel("File Details:");
    jtf=new JTextField();
    jtb=new JTable(data,head);
    jb=new JButton("Refresh");

    jtb.setShowGrid(false);
    jtb.setEnabled(false);

    root=new DefaultMutableTreeNode("Explorer");

    jt=new JTree(root);

    jt.setRootVisible(false);

    jp1=new JScrollPane(jt);
    jp2=new JScrollPane(jtb);

    jl1.setBounds(20,20,180,25);
    jl2.setBounds(20,70,100,25);
    jl3.setBounds(250,70,80,25);
    jtf.setBounds(200,20,520,25);
    jp1.setBounds(20,100,200,350);
    jp2.setBounds(250,100,450,350);
    jb.setBounds(300,470,100,30);

    jb.addActionListener(this);

    e1=new Explorer1(this);
    jt.addMouseListener(e1);

    jf.addWindowListener(this);
    jf.add(jl1);
    jf.add(jl2);
    jf.add(jl3);
    jf.add(jtf);
    jf.add(jp1);
    jf.add(jp2);
    jf.add(jb);
    jf.setLayout(null);
    jf.setSize(750,550);
    jf.setVisible(true);

  }

  public void actionPerformed(ActionEvent ae)
  {
    root.removeAllChildren();
    String s="",t="";
    String search="\\";
    String rep="\\\\";
    s=jtf.getText();
    int i=0,flag=0,k,vis=0;

    do
    {
      i=s.indexOf(search,i+2);
      if(i!=-1)
      {
        flag=1;
        t=s.substring(0,i);
        t=t+rep;
        t=t+s.substring(i+search.length());
        s=t;
      }
    }while(i!=-1);

    if(flag==0 && !s.equals(""))
      s=s+"\\";

    File f=new File(s);
    if(f.isDirectory())
    {
      if(flag==0)
      {
        k=s.indexOf('\\');
        t=s.substring(0,k);
        root.setUserObject(t);
        root1=root;
        directory(s);
      }

      if(flag==1)
      {
        k=s.lastIndexOf('\\');
        t=s.substring(k+1);
        if(!t.equals(""))
        {
          root.setUserObject(t);
          root1=root;
          directory(s);
        }
      }

      jt.setRootVisible(true);
    }
    if(!f.isDirectory() || t.equals(""))
    {
      jt.updateUI();
      jt.setRootVisible(false);
      JOptionPane.showMessageDialog(jf,"Drive/Directory not found!","ERROR",JOptionPane.ERROR_MESSAGE);
    }
    jt.updateUI();

  }


  public void directory(String str)
  {
    try
    {
      File f = new File(str);
      if(f.isDirectory())
      {
        String s[] = f.list();
        for(int i=0;i<s.length;i++)
        {
          File f1 = new File(str + "\\" + s[i]); 
          if(f1.isDirectory())
          {
            str1=""+s[i];
            root2=new DefaultMutableTreeNode(str1);
            root1.add(root2);
          }
        }
        for(int i=0;i<s.length;i++)
        {
          File f1 = new File(str + "/" + s[i]); 
          if(f1.isDirectory())
          {
            str1=""+s[i];
            root1=root1.getNextNode();
            directory(str + "/" + s[i]);
          }
        }
      }
    }catch(NullPointerException ne) { }
  }

  public void windowClosing(WindowEvent we)
  {
    System.exit(0);
  }

  public static void main(String s[])
  {
    new Explorer();
  } 
}