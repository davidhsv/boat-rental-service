-- Insert data into pet_owner table
INSERT INTO public.pet_owner (name, contact_info) VALUES
                                                      ('John Doe', 'john.doe@example.com'),
                                                      ('Jane Smith', 'jane.smith@example.com'),
                                                      ('Alice Johnson', 'alice.johnson@example.com');

-- Insert data into pet table
INSERT INTO public.pet (name, weight, breed, vaccinated, training_level, pet_owner_id) VALUES
                                                                                           ('Buddy', 20.0, 'Labrador', true, 5, 1),
                                                                                           ('Max', 15.0, 'Beagle', true, 7, 2),
                                                                                           ('Bella', 10.0, 'Terrier', false, 4, 3),
                                                                                           ('Charlie', 22.5, 'Poodle', true, 8, 1),
                                                                                           ('Lucy', 18.0, 'Bulldog', true, 6, 2),
                                                                                           ('Rocky', 24.0, 'Boxer', false, 3, 3),
                                                                                           ('Molly', 23.0, 'Poodle', true, 9, 1),
                                                                                           ('Daisy', 19.5, 'Shih Tzu', true, 2, 2),
                                                                                           ('Bailey', 12.0, 'Cocker Spaniel', true, 6, 3),
                                                                                           ('Lola', 25.0, 'Dachshund', false, 1, 1);
