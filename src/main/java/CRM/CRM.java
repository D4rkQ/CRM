package CRM;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
//import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

import java.io.*;

/**
 * CRM OWL Ontology
 * based on some Examples of E. Ammann *
 * by Marcus Danzer & Marcel Sailer @ reutlingen-university
        */

public class CRM {

    private static final String BASE_URL = "http://www.semanticweb.org/max/ontologies/CRM_Ontology";
    private static final String FILE_LOCATION = "CRM_Ontology.owl";
    private static File output = new File("CRM_Ontology_new.owl");

    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException
    {

        //prepare ontology
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File(FILE_LOCATION);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        OWLDataFactory factory = manager.getOWLDataFactory();
        PrefixOWLOntologyFormat pm = (PrefixOWLOntologyFormat) manager.getOntologyFormat(ontology);
        pm.setDefaultPrefix(BASE_URL + "#");

        IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
        System.out.println("\nOntology loaded from: " + documentIRI + "\n");

        OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();

        // Kunde wird erzeugt (Peter)
        OWLIndividual peter = factory.getOWLNamedIndividual(":Peter", pm);
        OWLClass kundenClass = factory.getOWLClass(":Kunde", pm);
        OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(kundenClass, peter);
        manager.addAxiom(ontology, caAxiom);

        //Mitarbeiter wird erzeugt (Georg)
        OWLIndividual georg = factory.getOWLNamedIndividual(":Georg", pm);
        OWLClass mitarbeiterClass = factory.getOWLClass(":Mitarbeiter", pm);
        OWLClassAssertionAxiom caAxiom2 = factory.getOWLClassAssertionAxiom(mitarbeiterClass, georg);
        manager.addAxiom(ontology, caAxiom2);

        //Property Assertion Georg betreut Peter
        OWLObjectProperty betreut = factory.getOWLObjectProperty(":betreut", pm);
        OWLObjectPropertyAssertionAxiom pAAxiom1 = factory.getOWLObjectPropertyAssertionAxiom(betreut, georg, peter);
        manager.addAxiom(ontology, pAAxiom1);

       //Property Assertion Hans betreut Peter (Sollte vom Reasoner bemängelt werden da Objection Restriktion besteht)
       OWLIndividual hans = factory.getOWLNamedIndividual(":Hans", pm);
       OWLObjectPropertyAssertionAxiom pAAxiom2 = factory.getOWLObjectPropertyAssertionAxiom(betreut, georg, hans);
       manager.addAxiom(ontology, pAAxiom2);

        //Die verschiedenen Personen als verschieden deklarieren(Wichtig das sonst Objection Restriktion nicht erkannt wird)
        OWLDifferentIndividualsAxiom dIAaxiom1 = factory.getOWLDifferentIndividualsAxiom(peter, georg, hans);
        manager.addAxiom(ontology, dIAaxiom1);

        //Reasoner erzeugen um Ontology zu validieren
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());

        //Valide Ontology speichern
            if (!reasoner.isConsistent()) {
            IRI documentIRI2 = IRI.create(output);
            manager.saveOntology(ontology, documentIRI2);
            System.out.println("\nExtended Ontology stored in " + documentIRI2 + "\n");
        } else{
            System.out.println("The created Ontology is not valid!! Please check constraints");
        }

    }
}