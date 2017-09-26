(#%require racket/trace)

(load "huffman.scm") 


;;Oppgave 1a
"Oppgave 1a:"
(define (p-cons x y)
  (lambda (proc) (proc x y)))

(define (p-car proc)
  (proc(lambda (x y)
         x)))
  
(define (p-cdr proc)
  (proc(lambda (x y)
         y)))
 
(p-cons "foo" "bar")
(p-car (p-cons "foo" "bar"))
(p-cdr (p-cons "foo" "bar"))
(p-car (p-cdr (p-cons "zoo" (p-cons "foo" "bar"))))


;;Oppgave 1b
"Oppgave 1b:"
(define foo 42)

(let ((foo 5)
      (x foo))
  (if (= x foo)
      'same
      'different))

((lambda(y x)
   (if (= x y)
       'same
       'different))
 5 foo)


(let ((bar foo)
      (baz 'towel))
  (let ((bar (list bar baz))
        (foo baz))
    (list foo bar)))

((lambda (bar baz)
   ((lambda (bar foo)
      (list foo bar))
    (list bar baz) baz))
 foo 'towel)


;;oppgave 1c:
"Oppgave 1c:"

(define foo (list 21 + 21))

(define baz (list 21 list 21))

(define bar (list 84 / 2))



(define (infix-eval exp)
  ((car(cdr exp)) (car exp) (car(cdr(cdr exp)))))

(infix-eval foo) 
(infix-eval baz)
(infix-eval bar)

;;Oppgave 1d:
;;Det som skjer når man bruker ' i stedet for list er
;;at / ikke blir lagret som en prosedyre i listen, men
;;et symbol. Derfor får vi error når vi prøver å bruke
;;det som en prosedyre som vi anvender på de andre elementene.


;;Oppgave 2a:
"Oppgave 2a:"

(define (member? pred a list)
  (if(null? list)
     #f
     (if(pred a (car list))
        #t
        (member? pred a (cdr list)))))

(member? eq? 'zoo '(bar foo zap))
(member? eq? 'foo '(bar foo zap))
(member? = 1 '(3 2 1 0))

(member? eq? '(1 bar)
         '((0 foo) (1 bar) (2 baz)))

(member? equal? '(1 bar)
         '((0 foo) (1 bar) (2 baz)))


;;Oppgave 2b:
"Oppgave 2b:"

;;Dette er fordi når vi har funnet en løvnode, er det viktig at vi begynner søket på
;;nytt fra roten, og ikke current branch, da dette vil gi feil svar.



;;Oppgave 2c:
"Oppgave 2c:e"



(define (decode-2 bits tree)
  (define (decode-2-1 bits current-branch items)
    (if (null? bits)
        items
        (let ((next-branch
               (choose-branch (car bits) current-branch)))
          (if (leaf? next-branch)
              (decode-2-1 (cdr bits) tree (append items (list (symbol-leaf next-branch))))
              (decode-2-1 (cdr bits) next-branch items)))))
  (decode-2-1 bits tree '()))

"Testing the 2 versions of 'decode'"
(decode-2 '(0 1 0 0 1 1 1 1 1 0) sample-tree)
(decode '(0 1 0 0 1 1 1 1 1 0) sample-tree)


;;Oppgave 2d:

;;Resultatet av dette kallet er å decode bitstrengen "sample-code" basert på kodeboken vår.
;;I dette tilfellet vil denne bitstrengen bli dekodet til "Ninjas fight ninjas by night"
;;når den anvendes på "sample-tree".

;;Opgpave 2e:
"Oppgave 2e:"

;; Finner ut om et symbol ligger i høyre eller venstre branch, og returnerer 0 eller 1 + branchen
(define (encode-branch symbol tree)
  (cond ((member? eq? symbol (symbols (left-branch tree))) (list 0 (left-branch tree)))
        ((member? eq? symbol (symbols (right-branch tree))) (list 1 (right-branch tree)))))

;;Kaller på encode-branch, og jobber seg nedover.
;;konser en verdi med neste kall helt til vi er på en løvnode.
(define (encode-symbol symbol tree)
  (if (leaf? tree) '()
      (let ((next-branch (encode-branch symbol tree)))
        (cons (car next-branch) (encode-symbol symbol (car(cdr next-branch)))))))


;;kaller på encode-symbol på alle symbolene i meldingen, og får tilbake en bitstreng.
(define (encode message tree)
  (if (null? message)
      '()
      (append (encode-symbol (car message) tree)
              (encode (cdr message) tree))))

(decode-2(encode '(ninjas fight ninjas by night) sample-tree) sample-tree)


;;oppgave 2f:
"Oppgave 2f:"
"need to doo"

;;Oppgave 2h:
"oppgave 2h:"
"need to doo"

;;Oppgave 2h:
"Oppgave 2h:"


