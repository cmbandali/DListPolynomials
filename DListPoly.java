public class DListPoly extends Polynomial {
   public static void main(String args[]) throws Exception {
      Polynomial p = new DListPoly(" X^5"),
                 q = new DListPoly("X^2 - X + 1");
      Utility.run(p, q);
   }
   public DListPoly(String s) {
     s = s.trim();
     for ( int i = 0; i < s.length(); i++ ) {
       char c = s.charAt(i);
       if( c == '+' || c == '-'){
         i++;
         c = s.charAt(i);
         if(c == ' ') s = s.substring(0,i) + s.substring(i+1,s.length());
       }
     }
     double coef;
     int degree;
     for( String n : s.split(" ") ){
       coef = 0.0; degree = 0;
       for(int i = 0; i < 1; i++){
         char c = n.charAt(i);
         if( c == '-' ){
            coef = -1.0; i++;
            c = n.charAt(i);
         }else if( c == '+' ){
            coef = 1.0; i++;
            c = n.charAt(i);
         }else if ( Character.isLetter(c) ) coef = 1.0;
         if( Character.isDigit(c) ){
            if( c == '.' || Character.isDigit(c) ){
             while( !Character.isLetter(c) && i < n.length() ){
               i++;
               if(i<n.length()) c = n.charAt(i);
             }
             if(n.charAt(0) == '+' || n.charAt(0) == '-') coef *= Double.parseDouble( n.substring(1,i) );
             else coef = Double.parseDouble( n.substring(0,i) );
           }
         }
         if( Character.isLetter(c) && i < n.length()-1){
           i+=2; c = n.charAt(i);
           if(Character.isDigit(c))degree = Integer.parseInt(n.substring(i,n.length()));
         }else if( Character.isLetter(c) && (i == n.length()-1) ) degree = 1;
         else degree = 0;
       }
       Term term = new Term( coef, degree );
       try{
         if (data.isEmpty()) data.addFirst(term);
         else {
            DList<Term> list = data;
            if( degree > list.getFirst().getData().getDegree() ) data.addFirst(term);
            else if(degree < list.getLast().getData().getDegree()) data.addLast(term);
            else{
             DNode<Term> node = list.getFirst();
             for( int i = 0; i < list.size(); i++){
               if( degree > node.getData().getDegree() ){
                 data.addBefore(term, node);i+=2;
               }
               if( degree == node.getData().getDegree() ){
                 double c = node.getData().getCoefficient() + coef;
                 term.setCoefficient(c); data.addAfter(term, node);
                 data.remove(node); i+=2;
               }
               node = list.getNext(node);
             }
           }
         }
       }catch(Exception e){
         System.out.println( e.getMessage() );
       }
     }
   }
   public DListPoly() {
      super();
   }
   public Polynomial add(Polynomial p) {
      Polynomial ans = new DListPoly();
      DNode<Term> one = null;
      DNode<Term> two = null;
      try{
        one = data.getFirst();
        two = p.data.getFirst();
      }catch(Exception e){
        System.out.println( e.getMessage() );
      }
      int size;
      if( data.size() > p.data.size() ){
        size = data.size();
        for(int i = p.data.size(); i<size; i++){
          Term term = new Term(0.0,0);
          p.data.addLast(term);
        }
      }
      else{
        size = p.data.size();
        for(int i = data.size(); i<size; i++){
          Term term = new Term(0.0,0);
          data.addLast(term);
        }
      }
      for( int i = 0; i < size; i++ ){
        if( one.getData().getDegree() > two.getData().getDegree() ){
          ans.data.addLast( one.getData() );
          if(i == size-1) if (two.getData() instanceof Term) ans.data.addLast( two.getData() );
          if(i == size-1){
            if (one.getNext().getData() instanceof Term){
              one = one.getNext();
              ans.data.addLast( one.getData() );
            }
          }
          if( data.size() == i ){
            while(p.data.size() != i){
              two = two.getNext();
              ans.data.addLast( two.getData() ); i++;
            }
          }else one = one.getNext();
        }
        else if( one.getData().getDegree() < two.getData().getDegree() ){
          ans.data.addLast( two.getData() );
          if(i == size-1){
            if (two.getNext().getData() instanceof Term){
              two = two.getNext();
              ans.data.addLast( two.getData() );
            }
          }
          if(i == size-1) if (one.getData() instanceof Term) ans.data.addLast( one.getData() );
          if( p.data.size() == i ){
            while(data.size() != i){
              one = one.getNext();
              ans.data.addLast( one.getData() ); i++;
            }
          }else two = two.getNext();
        }else if( one.getData().getDegree() == two.getData().getDegree() ){
          double coef = one.getData().getCoefficient() + two.getData().getCoefficient();
          int deg = one.getData().getDegree();
          Term term = new Term( coef, deg );
          ans.data.addLast(term);
          if(i == size-1){
            if (two.getNext().getData() instanceof Term){
              two = two.getNext();
              ans.data.addLast( two.getData() );
            }
            if (one.getNext().getData() instanceof Term){
              one = one.getNext();
              ans.data.addLast( one.getData() );
            }
          }
          if( p.data.size() == i ){
            while(data.size() != i){
              one = one.getNext();
              ans.data.addLast( one.getData() ); i++;
            }
          }else if( data.size() == i ){
            while(p.data.size() != i){
              two = two.getNext();
              ans.data.addLast( two.getData() ); i++;
            }
          }else one = one.getNext(); two = two.getNext();
        }
        try{
          if( (ans.data.size()>1) &&
            (ans.data.getLast().getPrev().getData().getDegree() == ans.data.getLast().getData().getDegree()) ){
            double coef = ans.data.getLast().getPrev().getData().getCoefficient() + ans.data.getLast().getData().getCoefficient();
            int deg = ans.data.getLast().getPrev().getData().getDegree();
            Term x = new Term(coef, deg);
            ans.data.remove( ans.data.getLast() );
            ans.data.remove( ans.data.getLast() );
            ans.data.addLast(x);
          }
        }catch(Exception e){
          System.out.println( e.getMessage() );
        }
      }
      return ans;
   }
   public Polynomial subtract(Polynomial p) {
      Polynomial ans = new DListPoly();
      DNode<Term> one = null;
      DNode<Term> two = null;
      try{
        one = data.getFirst();
        two = p.data.getFirst();
      }catch(Exception e){
        System.out.println( e.getMessage() );
      }
      int size; double coef;
      if( data.size() > p.data.size() ){
        size = data.size();
        for(int i = p.data.size(); i<size; i++){
          Term term = new Term(0.0,0);
          p.data.addLast(term);
        }
      }
      else{
        size = p.data.size();
        for(int i = data.size(); i<size; i++){
          Term term = new Term(0.0,0);
          data.addLast(term);
        }
      }
      for( int i = 0;i < size; i++ ){
        if( one.getData().getDegree() > two.getData().getDegree() ){
          ans.data.addLast( one.getData() );
          if(i == size-1){
            if (two.getData() instanceof Term){
              double co = -1 * two.getData().getCoefficient();
              Term t = new Term(co, two.getData().getDegree());
              ans.data.addLast( t );
              two = two.getNext(); i++;
            }
          }
          if(i == size-1 || i == size){
            if (one.getNext().getData() instanceof Term){
              one = one.getNext();
              ans.data.addLast( one.getData() );
            }
          }
          if( data.size()-1 == i ){
            while(p.data.size() != i){
              double c = -1 * two.getData().getCoefficient();
              Term term = new Term(c,two.getData().getDegree());
              ans.data.addLast( term );
              two = two.getNext(); i++;
            }
          }else one = one.getNext();
        }
        else if( one.getData().getDegree() < two.getData().getDegree() ){
          double c = -1 * two.getData().getCoefficient();
          Term term = new Term(c,two.getData().getDegree());
          ans.data.addLast( term );
          if(i == size-1){
            if (two.getNext().getData() instanceof Term){
              two = two.getNext();
              double co = -1 * two.getData().getCoefficient();
              Term t = new Term(co, two.getData().getDegree());
              ans.data.addLast( t );
            }
          }
          if(i == size-1) if (one.getData() instanceof Term) ans.data.addLast( one.getData() );
          if( p.data.size() == i ){
            ans.data.addLast( one.getData() );
            one = one.getNext();
            while(data.size() != i ){
              ans.data.addLast( one.getData() );
              one = one.getNext(); i++;
            }
          }else two = two.getNext();
        }
        else if( one.getData().getDegree() == two.getData().getDegree() ){
            coef = one.getData().getCoefficient() - two.getData().getCoefficient();
          int deg;
          if(coef == 0) deg = 0;
          else {
            deg = one.getData().getDegree();
            Term term = new Term( coef, deg );
            ans.data.addLast(term);
          }
          if(i == size-1){
            if (two.getNext().getData() instanceof Term){
              two = two.getNext();
              double co = -1 * two.getData().getCoefficient();
              Term t = new Term(co, two.getData().getDegree());
              ans.data.addLast( t );
            }
            if (one.getNext().getData() instanceof Term){
              one = one.getNext();
              ans.data.addLast( one.getData() );
            }
          }
          if( p.data.size() == i ){
            while(data.size() != i){
              one = one.getNext();
              ans.data.addLast( one.getData() ); i++;
            }
          }
          else if( data.size() == i ){
            while(p.data.size() != i){
              two = two.getNext();
              double c = -1 * two.getData().getCoefficient();
              Term term = new Term(c,two.getData().getDegree());
              ans.data.addLast( term );i++;
            }
          }else one = one.getNext(); two = two.getNext();
        }
        try{
          if( (ans.data.size()>1) &&
              (ans.data.getLast().getPrev().getData().getDegree() == ans.data.getLast().getData().getDegree()) ){
            double c = ans.data.getLast().getPrev().getData().getCoefficient() + ans.data.getLast().getData().getCoefficient();
            ans.data.getLast().getPrev().getData().setCoefficient(c);
            ans.data.remove( ans.data.getLast() );
          }
        }catch(Exception e){
          System.out.println( e.getMessage() );
        }
      }
      return ans;
   }
   public Polynomial multiply(Polynomial p) {
      Polynomial ans = new DListPoly();
      Polynomial temp = new DListPoly();
      DNode<Term> one = null;
      DNode<Term> two = null;
      try{
        one = data.getFirst();
        two = p.data.getFirst();
      }catch(Exception e){
        System.out.println( e.getMessage() );
      }
      int size, deg; double coef;
      for( int i = 0; i < data.size(); i++ ){
        for( int j = 0; j < p.data.size(); j++ ){
          coef = one.getData().getCoefficient() * two.getData().getCoefficient();
          deg = one.getData().getDegree() + two.getData().getDegree();
          Term term = new Term(coef, deg);
          if(ans.data.size()>0){
            temp.data.addFirst(term);
            ans = ans.add(temp);
            try{ temp.data.remove(temp.data.getFirst());
            }catch(Exception e){System.out.println(e.getMessage());}
          }else ans.data.addLast(term);
          two = two.getNext();
        }
        one = one.getNext();
        try{
          two = p.data.getFirst();
        }catch(Exception e){
          System.out.println( e.getMessage() );
        }
      }
      return ans;
   }
   public Polynomial divide(Polynomial p) throws Exception {
      Polynomial ans = new DListPoly();
      Polynomial num = new DListPoly(); num.data = data;
      Polynomial den = new DListPoly(); den.data = p.data;
      DList<Term> numerator = num.data;
      DList<Term> denominator = den.data;
      Term nTerm = numerator.getFirst().getData();
      Term dTerm = denominator.getFirst().getData();
      while( nTerm.getDegree() >= dTerm.getDegree() ){
        int d = nTerm.getDegree() - dTerm.getDegree();
        double c = nTerm.getCoefficient()/dTerm.getCoefficient();
        Term term = new Term(c,d);
        ans.data.addLast(term);
        Polynomial temp = new DListPoly(ans.data.getLast().getData().toString());
        Polynomial minus = temp.multiply(den);
        num = num.subtract(minus);
        numerator = num.data;
        nTerm = numerator.getFirst().getData();
      }
      return ans;
   }
   public Polynomial remainder(Polynomial p) throws Exception {
     Polynomial quotient = new DListPoly();
     Polynomial rem = new DListPoly(); rem.data = data;
     Polynomial den = new DListPoly(); den.data = p.data;
     DList<Term> remainder = rem.data;
     DList<Term> denominator = den.data;
     Term nTerm = remainder.getFirst().getData();
     Term dTerm = denominator.getFirst().getData();
     while( nTerm.getDegree() >= dTerm.getDegree() ){
       int d = nTerm.getDegree() - dTerm.getDegree();
       double c = nTerm.getCoefficient()/dTerm.getCoefficient();
       Term term = new Term(c,d);
       quotient.data.addLast(term);
       Polynomial temp = new DListPoly(quotient.data.getLast().getData().toString());
       Polynomial minus = temp.multiply(den);
       rem = rem.subtract(minus);
       remainder = rem.data;
       nTerm = remainder.getFirst().getData();
     }
      return rem;
   }
}
